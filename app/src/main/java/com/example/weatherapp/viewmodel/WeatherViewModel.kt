package com.example.weatherapp.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.forecastModel.Forecast
import com.example.core.data.model.CurrentWeather
import com.example.core.implementation.WeatherRepositoryImplementation
import com.example.core.repository.WeatherRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherDataRepo:WeatherRepositoryInterface
):ViewModel(){
    val weatherData = MutableLiveData<CurrentWeather>()
    val forecastData = MutableLiveData<Forecast>()

    fun getWeatherDataFromCity(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = weatherDataRepo.getWeatherDataCity(city)
            weatherData.postValue(data!!)
        }
    }
    fun getWeatherDataFromLoc(lat:Double, lon:Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = weatherDataRepo.getWeatherDataLoc(lat, lon)
            weatherData.postValue(data!!)
        }
    }
    fun getForecastDataFromCity(city:String){
        viewModelScope.launch(Dispatchers.IO) {
            val data = weatherDataRepo.getForecastDataCity(city)
            forecastData.postValue(data!!)
        }
    }
    fun getForecastDataFromLoc(lat: Double, lon: Double){
        viewModelScope.launch(Dispatchers.IO) {
            val data = weatherDataRepo.getForecastDataLoc(lat, lon)
            forecastData.postValue(data!!)
        }
    }
}