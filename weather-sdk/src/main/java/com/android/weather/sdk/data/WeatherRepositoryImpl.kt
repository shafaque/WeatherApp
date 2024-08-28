package com.android.weather.sdk.data

import com.android.weather.sdk.data.response.HourlyForecastResponse
import com.android.weather.sdk.data.response.WeatherDataResponse
import com.android.weather.sdk.data.network.RetrofitClient

internal class WeatherRepositoryImpl : WeatherRepository {

    override suspend fun getCurrentWeather(city: String, apiKey: String): WeatherDataResponse {
        return RetrofitClient.getService().getCurrentWeather(city, apiKey)
    }

    override suspend fun getHourlyWeather(city: String, apiKey: String): HourlyForecastResponse {
        return RetrofitClient.getService().getHourlyForecast(city, apiKey)
    }
}