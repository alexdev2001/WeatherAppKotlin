package com.example.weatherapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherInfo(
        @Query("q") city: String,
        @Query("appid") key: String,
        @Query("units") units: String = "metric"
    )
}