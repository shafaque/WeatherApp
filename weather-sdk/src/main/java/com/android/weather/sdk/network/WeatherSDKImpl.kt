package com.android.weather.sdk.network

import androidx.fragment.app.Fragment
import com.android.weather.sdk.ui.ForecastFragment

// Implementation of the WeatherSDK interface.
class WeatherSDKImpl : WeatherSDK {

    // API key for accessing weather data.private late init var apiKey: String
    private lateinit var apiKey: String

    // Listener for receiving weather-related events.
    private lateinit var listener: WeatherSDKListener

    // Initializes the SDK with the provided API key.
    override fun initialize(apiKey: String) {
        this.apiKey = apiKey
    }
    // Launches a fragment to display weather information for the given city.
    override fun launch(cityName: String): Fragment {
        return ForecastFragment.newInstance(apiKey, cityName, listener)
    }

    // Sets the listener for receiving weather-related events.
    override fun setWeatherSDKListener(listener: WeatherSDKListener) {
        this.listener = listener
    }
}
