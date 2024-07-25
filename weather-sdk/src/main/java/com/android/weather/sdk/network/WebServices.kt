package com.android.weather.sdk.network

import com.android.weather.sdk.data.HourlyForecastResponse
import com.android.weather.sdk.data.WeatherDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {


    // Endpoint to get the current weather data for a given city.
    @GET("current")
    suspend fun getCurrentWeather(
        @Query("city") city: String,
        @Query("key") apiKey: String
    ): WeatherDataResponse

    // Endpoint to get the hourly weather forecast for a given city.
    @GET("forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("city") city: String,
        @Query("key") apiKey: String
    ): HourlyForecastResponse
}