package com.android.weather.sdk

import androidx.fragment.app.Fragment
import com.android.weather.sdk.presentation.ui.ForecastFragment

/**
 * Implementation of the [WeatherSDK] interface.
 */
class WeatherSDKImpl : WeatherSDK {

    // API key for accessing weather data.
    private lateinit var apiKey: String

    // Listener for receiving weather-related events.
    private lateinit var listener: WeatherSDKListener

    /**
     * Initializes the SDK with the provided API key.
     *
     * @param apiKey The API key for accessing weather data.
     */
    override fun initialize(apiKey: String) {
        this.apiKey = apiKey
    }

    /**
     * Launches a fragment to display weather information for the given city.
     *
     * @param cityName The name of the city for which to display weather information.
     * @return A [Fragment] displaying the weather information for the given city.
     */
    override fun launch(cityName: String): Fragment {
        return ForecastFragment.newInstance(apiKey, cityName, listener)
    }

    /**
     * Sets the listener for receiving weather-related events.
     *
     * @param listener The [WeatherSDKListener] to receive weather-related events.
     */
    override fun setWeatherSDKListener(listener: WeatherSDKListener) {
        this.listener = listener
    }
}
