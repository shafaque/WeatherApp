package com.android.weather.sdk.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weather.sdk.data.WeatherRepository
import com.android.weather.sdk.data.WeatherRepositoryImpl
import com.android.weather.sdk.data.response.HourlyForecastResponse
import com.android.weather.sdk.data.response.WeatherDataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for managing weather data and UI states.
 *
 * This ViewModel uses coroutines to fetch weather and hourly forecast data from the [WeatherRepository].
 * The data is exposed to the UI via [StateFlow]s representing different states such as loading, success, and error.
 *
 * @param weatherRepository A repository to fetch weather and forecast data.
 * @param ioDispatcher The [CoroutineDispatcher] used for IO-bound tasks, with [Dispatchers.IO] as the default.
 */
internal class WeatherViewModel(
    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    // StateFlow for holding the UI state of current weather data, initialized in the Loading state
    private val _currentWeatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val currentWeatherState: StateFlow<WeatherState> get() = _currentWeatherState.asStateFlow()

    // StateFlow for holding the UI state of hourly forecast data, initialized in the Loading state
    private val _hourlyForecastState = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val hourlyForecastState: StateFlow<ForecastState> get() = _hourlyForecastState.asStateFlow()

    // StateFlow for holding any error messages encountered during data fetching
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = _errorState.asStateFlow()


    /**
     * Fetches current weather and hourly forecast data for the given city.
     *
     * This function launches a coroutine to perform two network calls:
     * one to get the current weather and another to get the hourly forecast.
     * Both results are published in their respective [StateFlow]s, and errors are captured and published in error flows.
     *
     * @param cityName The name of the city for which to fetch weather data.
     * @param apiKey The API key required to access the weather API.
     */
    fun fetchWeatherData(cityName: String, apiKey: String) {
        viewModelScope.launch {
            try {
                // Fetch current weather data asynchronously on the IO dispatcher
                val currentWeather = withContext(ioDispatcher) {
                    weatherRepository.getCurrentWeather(cityName, apiKey)
                }
                // Update the state with successfully fetched weather data
                _currentWeatherState.value = WeatherState.Success(currentWeather)

                // Fetch hourly forecast data asynchronously on the IO dispatcher
                val hourlyForecast = withContext(ioDispatcher) {
                    weatherRepository.getHourlyWeather(cityName, apiKey)
                }
                // Update the state with successfully fetched hourly forecast data
                _hourlyForecastState.value = ForecastState.Success(hourlyForecast)

            } catch (e: Exception) {
                // Handle errors by updating both weather and forecast states with error messages
                val errorMessage = e.message ?: "Unknown error"
                _errorState.value = errorMessage
                _currentWeatherState.value = WeatherState.Error(errorMessage)
                _hourlyForecastState.value = ForecastState.Error(errorMessage)
            }
        }
    }
}

/**
 * Sealed class representing the state of current weather data.
 *
 * This class is used to represent different UI states for the current weather:
 * loading, success (with weather data), and error (with an error message).
 */
internal sealed class WeatherState {
    /** Represents the loading state where weather data is being fetched. */
    data object Loading : WeatherState()

    /** Represents the success state with the loaded weather data. */
    data class Success(val data: WeatherDataResponse) : WeatherState()

    /** Represents the error state with an error message when data fetching fails. */
    data class Error(val message: String) : WeatherState()
}

/**
 * Sealed class representing the state of the hourly forecast data.
 *
 * This class is used to represent different UI states for the hourly forecast:
 * loading, success (with forecast data), and error (with an error message).
 */
internal sealed class ForecastState {
    /** Represents the loading state where forecast data is being fetched. */
    data object Loading : ForecastState()

    /** Represents the success state with the loaded hourly forecast data. */
    data class Success(val data: HourlyForecastResponse) : ForecastState()

    /** Represents the error state with an error message when data fetching fails. */
    data class Error(val message: String) : ForecastState()
}
