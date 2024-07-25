package com.android.weather.sdk.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weather.sdk.data.HourlyForecastResponse
import com.android.weather.sdk.data.WeatherDataResponse
import com.android.weather.sdk.network.RetrofitClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    // Function to fetch weather data using coroutines and StateFlow
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

// Sealed classes for representing weather data states
sealed class WeatherState {
    data object Loading : WeatherState()
    data class Success(val data: WeatherDataResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

// Sealed classes for representing forecast data states
sealed class ForecastState {
    data object Loading : ForecastState()
    data class Success(val data: HourlyForecastResponse) : ForecastState()
    data class Error(val message: String) : ForecastState()
}
