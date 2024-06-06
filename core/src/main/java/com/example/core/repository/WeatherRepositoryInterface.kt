package com.example.core.repository

import android.content.Context
import com.example.core.data.forecastModel.Forecast
import com.example.core.data.model.CurrentWeather

interface WeatherRepositoryInterface {

    suspend fun getWeatherDataCity(city: String):CurrentWeather?

    suspend fun getWeatherDataLoc(lat:Double, lon:Double):CurrentWeather?

    suspend fun getForecastDataCity(city: String):Forecast?

    suspend fun getForecastDataLoc(lat: Double, lon: Double):Forecast?

}