package com.android.weather.sdk.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory class for creating instances of [WeatherViewModel].
 *
 * @param cityName The name of the city for which to fetch weather data.
 * @param apiKey The API key for accessing weather data.
 */
class WeatherViewModelFactory(
    private val cityName: String,
    private val apiKey: String
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given [modelClass].
     *
     * @param modelClass A [Class] whose instance is requested.
     * @return A newly created [ViewModel] instance.
     * @throws IllegalArgumentException if the given [modelClass] is not assignable from [WeatherViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(cityName, apiKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
