package com.example.core.implementation

import com.example.core.data.WeatherDataSource
import com.example.core.data.forecastModel.Forecast
import com.example.core.data.model.CurrentWeather
import com.example.core.repository.WeatherRepositoryInterface
import dagger.hilt.EntryPoint
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WeatherRepositoryImplementation @Inject constructor(
    val dataSource: WeatherDataSource
): WeatherRepositoryInterface {

    var weatherDataCache: CurrentWeather? = null
    var forecastDataCache: Forecast? =null
//    For temperature in Fahrenheit use units=imperial
//    For temperature in Celsius use units=metric
//    Temperature in Kelvin is used by default, no need to use units parameter in API call

    override suspend fun getWeatherDataCity(city: String): CurrentWeather? {
        return dataSource.getCityData(city)
    }

    override suspend fun getWeatherDataLoc(lat: Double, lon: Double): CurrentWeather? {
        return dataSource.getLocData(lat,lon)
    }

    override suspend fun getForecastDataCity(city: String): Forecast? {
        return dataSource.getForecastCity(city)
    }

    override suspend fun getForecastDataLoc(lat: Double, lon: Double): Forecast? {
        return dataSource.getForecastLoc(lat,lon)
    }
}