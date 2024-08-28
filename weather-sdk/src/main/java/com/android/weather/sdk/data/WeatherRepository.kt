package com.android.weather.sdk.data

import com.android.weather.sdk.data.response.HourlyForecastResponse
import com.android.weather.sdk.data.response.WeatherDataResponse

 internal interface WeatherRepository {
    suspend fun getCurrentWeather(
        city: String,
        apiKey: String,
    ): WeatherDataResponse

    suspend fun getHourlyWeather(
        city: String,
        apiKey: String,
    ): HourlyForecastResponse
}