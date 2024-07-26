package com.android.weather.sdk.network

import com.android.weather.sdk.data.HourlyForecastResponse
import com.android.weather.sdk.data.WeatherDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface representing the web services for fetching weather data.
 */
interface WebServices {

    /**
     * Endpoint to get the current weather data for a given city.
     *
     * @param city The name of the city for which to fetch current weather data.
     * @param apiKey The API key for accessing the weather service.
     * @return A [WeatherDataResponse] object containing the current weather data.
     */
    @GET("current")
    suspend fun getCurrentWeather(
        @Query("city") city: String,
        @Query("key") apiKey: String
    ): WeatherDataResponse

    /**
     * Endpoint to get the hourly weather forecast for a given city.
     *
     * @param city The name of the city for which to fetch the hourly forecast.
     * @param apiKey The API key for accessing the weather service.
     * @return A [HourlyForecastResponse] object containing the hourly weather forecast.
     */
    @GET("forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("city") city: String,
        @Query("key") apiKey: String
    ): HourlyForecastResponse
}
