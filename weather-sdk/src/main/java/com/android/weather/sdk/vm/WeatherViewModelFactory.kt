package com.android.weather.sdk.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory(
    private val cityName: String,
    private val apiKey: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(cityName, apiKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
