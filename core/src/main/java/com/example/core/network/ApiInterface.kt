package com.example.core.network

import com.example.core.data.forecastModel.Forecast
import com.example.core.data.model.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather?")
    suspend fun getCurrentWeatherCity(
        @Query("q") city : String,
        @Query("units") units : String,
        @Query("appid") appid : String,
    ): Response<CurrentWeather>

    @GET("forecast?")
    suspend fun getForecastCity(
        @Query("q") city:String,
        @Query("units") units : String,
        @Query("appid") appid : String,
    ):Response<Forecast>

    @GET("weather?")
    suspend fun getCurrentWeatherLoc(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("units") units : String,
        @Query("appid") appid : String,
    ): Response<CurrentWeather>

    @GET("forecast?")
    suspend fun getForecastLoc(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("units") units : String,
        @Query("appid") appid : String,
    ):Response<Forecast>

}