package com.example.core.data

import android.content.Context
import android.widget.Toast
import com.example.core.R
import com.example.core.data.forecastModel.Forecast
import com.example.core.data.model.CurrentWeather
import com.example.core.network.ApiInterface
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherDataSource @Inject constructor(
    private val api:ApiInterface,
    private val context: Context
) {

    var weatherDataCache: CurrentWeather? = null
    var forecastDataCache: Forecast? =null
    var weatherDataLocCache:CurrentWeather? = null
    var forecastDataLocCache:Forecast? = null
//    For temperature in Fahrenheit use units=imperial
//    For temperature in Celsius use units=metric
//    Temperature in Kelvin is used by default, no need to use units parameter in API call

     suspend fun getCityData(city: String): CurrentWeather? {
        // check if cache is null
        if (weatherDataCache != null){
            return weatherDataCache
        } else {
            // fetch data from api
            val response = try{
                api.getCurrentWeatherCity(city, "metric", context.getString(R.string.api_key))
            }
            catch (e: IOException){
                Toast.makeText(context, "app error ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            catch (e: HttpException){
                Toast.makeText(context, "http error ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            // put data in cache
            if (response.isSuccessful && response.body() != null){
                val data = response.body()!!
                weatherDataCache = data
                // return cache data
                return weatherDataCache
            }
        }
        return weatherDataCache
    }


     suspend fun getLocData(lat:Double, lon:Double):CurrentWeather?{
        // check if cache is null
        if (weatherDataLocCache != null){
            // location is null
            return weatherDataLocCache

        } else {
            // fetch data from api
            val response = try{
                api.getCurrentWeatherLoc(lat,lon, "metric", context.getString(R.string.api_key))
            }
            catch (e: IOException){
                Toast.makeText(context, "app error ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            catch (e: HttpException){
                Toast.makeText(context, "http error ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            // put data in cache
            if (response.isSuccessful && response.body() != null){
                val data = response.body()!!
                weatherDataLocCache = data
                // return cache data
                return weatherDataLocCache
            }
        }
        return weatherDataLocCache
    }



     suspend fun getForecastCity(city: String):Forecast?{
        if (forecastDataCache != null){
            return forecastDataCache
        } else {
            // fetch data from api
            val response = try{
                api.getForecastCity(city, "metric", context.getString(R.string.api_key))
            }
            catch (e: IOException){
                Toast.makeText(context, "app error ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            catch (e: HttpException){
                Toast.makeText(context, "http error ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            // put data in cache
            if (response.isSuccessful && response.body() != null){
                val data = response.body()!!
                forecastDataCache = data
                // return cache data
                return forecastDataCache
            }
        }
        return forecastDataCache
    }

     suspend fun getForecastLoc(lat: Double, lon: Double):Forecast?{
        if (forecastDataLocCache != null){
            return forecastDataLocCache
        } else {
            // fetch data from api
            val response = try{
                api.getForecastLoc(lat,lon, "metric", context.getString(R.string.api_key))
            }
            catch (e: IOException){
                Toast.makeText(context, "app error ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            catch (e: HttpException){
                Toast.makeText(context, "http error ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            // put data in cache
            if (response.isSuccessful && response.body() != null){
                val data = response.body()!!
                forecastDataLocCache = data
                // return cache data
                return forecastDataLocCache
            }
        }
        return forecastDataLocCache
    }

}