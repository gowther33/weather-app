package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.forecastModel.Forecast
import com.example.core.data.forecastModel.ForecastData
import com.example.core.data.model.CurrentWeather
import com.example.weatherapp.adapter.RvAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.BottomSheetLayoutBinding
import com.example.weatherapp.utils.LocationManagerUtil
import com.example.weatherapp.utils.PermissionsHandler
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetBinding:BottomSheetLayoutBinding
    private lateinit var dialog: BottomSheetDialog

    private val viewModel :WeatherViewModel by viewModels()
    private var city = "London"
    private var lat:Double? = null
    private var lon:Double? = null

    private lateinit var locManager:LocationManagerUtil
    private val REQUEST_LOCATION_PERMISSION = 200
    private val permissionHandler = PermissionsHandler(this)

    val TAG = "Weather Data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sheetBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        dialog.setContentView(sheetBinding.root)

        locManager = LocationManagerUtil(this, 5000L, 5F)

        // fetch default data
        if(lat != null && lon != null){
            viewModel.getWeatherDataFromLoc(lat!!,lon!!)
        }else {
            viewModel.getWeatherDataFromCity(city)
        }

        viewModel.weatherData.observe(this) { data ->
            Log.d(TAG, "Weather data updated!")
            updateWeatherData(data)
        }

        viewModel.forecastData.observe(this) { forecastData ->
            openDialog(forecastData)
        }

        binding.tvForecast.setOnClickListener{
            if (lat != null && lon != null){
                viewModel.getForecastDataFromLoc(lat!!, lon!!)
                Log.d(TAG, "Location Data! $lat, $lon")
            }else {
                viewModel.getForecastDataFromCity(city)
            }
        }

        binding.tvLocation.setOnClickListener {
            if (lat == null && lon == null){
                getLocation()
                Log.d(TAG, "Location fetched! $lat, $lon")
            }else{
                viewModel.getWeatherDataFromLoc(lat!!, lon!!)
                Log.d(TAG, "Location Data! $lat, $lon")
            }
        }
    }

    // Bottom sheet dialog for forecast data
    @SuppressLint("SetTextI18n")
    private fun openDialog(data: Forecast) {
        val forecastArray: ArrayList<ForecastData> = data.list as ArrayList<ForecastData>

        sheetBinding.rvForecast.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 1, RecyclerView.HORIZONTAL, false)
            adapter = RvAdapter(forecastArray)
        }
        sheetBinding.tvSheet.text = "Five days forecast in ${data.city.name}"
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
    }

    // Location fetching logic
//    private fun fetchLocation() {
//        val task: Task<Location> = fusedLocationProviderClient.lastLocation
//        if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ){
//            return
//        }
//        task.addOnSuccessListener {
//            lat = it.latitude
//            lon = it.longitude
//            val geocoder = Geocoder(this, Locale.getDefault())
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//                geocoder.getFromLocation(it.latitude, it.longitude, 1
//                ) { addresses ->
//                    city = addresses[0].locality
//                    Log.d(TAG, "Location fetched city: $city")
//                }
//            }else{
//                val address = geocoder.getFromLocation(it.latitude, it.longitude, 1) as List<Address>
//                city = address[0].locality
//                Log.d(TAG, "Location fetched city: $city")
//            }
//        }
//    }

    private fun getLocation() {

        //starting of android 6 ,if permission is not granted ,we havta request it at runtime
        if (!permissionHandler.checkPermission()) {
            permissionHandler.askLocPermission(REQUEST_LOCATION_PERMISSION)
            return
        }

        //location already grandted...

        //try to get last know location
        locManager.locationClient.lastLocation.addOnSuccessListener { loc ->
            if (loc != null){
                lat = loc.latitude
                lon = loc.longitude
            }
            else {
                //last know location is NULL
                //request location updates
                if (permissionHandler.checkGPS()) {
                    locManager.startLocationTracking()
                    return@addOnSuccessListener
                }else{
                    permissionHandler.requestLocationService()
                    return@addOnSuccessListener
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeatherData(data:CurrentWeather){
        Log.d(TAG, "City: ${data.name}")
        val iconId = data.weather[0].icon
        val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"
        Picasso.get().load(imgUrl).into(binding.imgWeather)

        binding.tvSunrise.text =
            SimpleDateFormat(
                "hh:mm a",
                Locale.ENGLISH,
            ).format(data.sys.sunrise * 1000)

        binding.tvSunset.text =
            SimpleDateFormat(
                "hh:mm a",
                Locale.ENGLISH
            ).format(data.sys.sunset * 1000)

        binding.apply {
            tvStatus.text = data.weather[0].description
            tvWind.text = "${data.wind.speed} KM/H"
            tvLocation.text = "${data.name}\n${data.sys.country}"
            tvTemp.text = "${data.main.temp.toInt()}°C"
            tvFeelsLike.text = "Feels like: ${data.main.feels_like.toInt()}°C"
            tvMinTemp.text = "Min temp: ${data.main.temp_min.toInt()}°C"
            tvMaxTemp.text = "Max temp: ${data.main.temp_max.toInt()}°C"
            tvHumidity.text = "${data.main.humidity} %"
            tvPressure.text = "${data.main.pressure} hPa"
            tvUpdateTime.text = "Last Update: ${
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.ENGLISH
                ).format(data.dt * 1000)
            }"
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.size >= 0 ) {

            val fine = grantResults[0]
            val coarse = grantResults[1]

            if (
                fine == PackageManager.PERMISSION_GRANTED
                &&
                coarse == PackageManager.PERMISSION_GRANTED
            ) {
                getLocation()
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
            }
        }
    }
}