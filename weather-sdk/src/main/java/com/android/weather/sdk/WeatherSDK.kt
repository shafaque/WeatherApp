package com.android.weather.sdk

import androidx.fragment.app.Fragment

/**
 * Interface defining the contract for a Weather SDK.
 */
interface WeatherSDK {

    /**
     * Initializes the SDK with the provided API key.
     *
     * @param apiKey The API key for accessing weather data.
     */
    fun initialize(apiKey: String)

    /**
     * Launches a UI component (Fragment) to display weather information for the specified city.
     *
     * @param cityName The name of the city for which to display weather information.
     * @return A [Fragment] displaying the weather information for the specified city.
     */
    fun launch(cityName: String): Fragment

    /**
     * Sets a listener to receive events and updates from the SDK.
     *
     * @param listener The [WeatherSDKListener] to receive events and updates.
     */
    fun setWeatherSDKListener(listener: WeatherSDKListener)
}

/**
 * Interface defining the methods a listener should implement to receive events from the Weather SDK.
 */
interface WeatherSDKListener {

    /**
     * Called when an operation completes successfully.
     */
    fun onFinished()

    /**
     * Called when an operation encounters an error.
     *
     * @param error A description of the error.
     */
    fun onFinishedWithError(error: String)
}
