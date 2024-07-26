package com.android.weather.sdk

import androidx.fragment.app.Fragment

// Interface defining the contract for a Weather SDK.
interface WeatherSDK {

    // Initializes the SDK with the provided API key.fun initialize(apiKey: String)
    fun initialize(apiKey: String)

    // Launches a UI component (Fragment) to display weather information for the specified city.
    fun launch(cityName: String): Fragment

    // Sets a listener to receive events and updates from the SDK.
    fun setWeatherSDKListener(listener: WeatherSDKListener)
}

// Interface defining the methods a listener should implement to receive events from the Weather SDK.
interface WeatherSDKListener {

    // Called when an operation completes successfully.
    fun onFinished()

    // Called when an operation encounters an error.
    // @param error A description of the error.
    fun onFinishedWithError(error: String)
}
