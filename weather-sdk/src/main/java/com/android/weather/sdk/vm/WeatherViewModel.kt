package com.android.weather.sdk.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weather.sdk.data.HourlyForecastResponse
import com.android.weather.sdk.data.WeatherDataResponse
import com.android.weather.sdk.network.RetrofitClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for managing weather data.
 *
 * @param cityName The name of the city for which to fetch weather data.
 * @param apiKey The API key for accessing weather data.
 * @param ioDispatcher The [CoroutineDispatcher] for IO operations, defaulting to [Dispatchers.IO].
 */
class WeatherViewModel(
    private val cityName: String,
    private val apiKey: String,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO // Dependency injection for testing
) : ViewModel() {

    // StateFlow for holding the UI state of current weather
    private val _currentWeatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val currentWeatherState: StateFlow<WeatherState> get() = _currentWeatherState.asStateFlow()

    // StateFlow for holding the UI state of hourly forecast
    private val _hourlyForecastState = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val hourlyForecastState: StateFlow<ForecastState> get() = _hourlyForecastState.asStateFlow()

    // StateFlow for holding any error messages
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = _errorState.asStateFlow()

    init {
        fetchWeatherData()
    }

    /**
     * Fetches weather data using coroutines and updates the state flows.
     */
    private fun fetchWeatherData() {
        viewModelScope.launch {
            try {
                // Fetch current weather data
                val currentWeather = withContext(ioDispatcher) {
                    RetrofitClient.getService().getCurrentWeather(cityName, apiKey)
                }
                // Update current weather state
                _currentWeatherState.value = WeatherState.Success(currentWeather)

                // Fetch hourly forecast data
                val hourlyForecast = withContext(ioDispatcher) {
                    RetrofitClient.getService().getHourlyForecast(cityName, apiKey)
                }
                // Update hourly forecast state
                _hourlyForecastState.value = ForecastState.Success(hourlyForecast)

            } catch (e: Exception) {
                // Update error state
                _errorState.value = e.message ?: "Unknown error"
                _currentWeatherState.value = WeatherState.Error(e.message ?: "Unknown error")
                _hourlyForecastState.value = ForecastState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

/**
 * Sealed class representing the state of the current weather data.
 */
sealed class WeatherState {
    /** Represents the loading state. */
    data object Loading : WeatherState()

    /** Represents the success state with the loaded weather data. */
    data class Success(val data: WeatherDataResponse) : WeatherState()

    /** Represents the error state with an error message. */
    data class Error(val message: String) : WeatherState()
}

/**
 * Sealed class representing the state of the hourly forecast data.
 */
sealed class ForecastState {
    /** Represents the loading state. */
    data object Loading : ForecastState()

    /** Represents the success state with the loaded forecast data. */
    data class Success(val data: HourlyForecastResponse) : ForecastState()

    /** Represents the error state with an error message. */
    data class Error(val message: String) : ForecastState()
}
