package com.android.weather.sdk.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.weather.sdk.data.HourlyForecastResponse
import com.android.weather.sdk.data.WeatherDataResponse
import com.android.weather.sdk.network.WeatherSDKListener
import com.android.weather.sdk.vm.ForecastState
import com.android.weather.sdk.vm.WeatherState
import com.android.weather.sdk.vm.WeatherViewModel
import com.android.weather.sdk.vm.WeatherViewModelFactory
import com.shaf.weather_sdk.databinding.FragmentForecastBinding
import kotlinx.coroutines.launch

class ForecastFragment : Fragment() {
    // Declare private properties for API key, city name, and listener
    private lateinit var apiKey: String
    private lateinit var cityName: String
    private lateinit var listener: WeatherSDKListener

    // View binding for the fragment's layout
    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    // Adapter for the hourly forecast RecyclerView
    private lateinit var adapter: HourlyForecastAdapter

    // ViewModel instance using delegated property with a custom factory
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(cityName, apiKey)
    }

    // Inflate the fragment's layout and return the root view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater,container, false)
        return binding.root
    }

    // Set up UI elements and fetch weather data after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView with an empty adapter
        adapter = HourlyForecastAdapter(emptyList())
        binding.hourlyForecastList.adapter = adapter

        // Fetch and display weather data
        fetchWeatherData()
    }

    /**
     * Fetches weather data and updates the UI.
     */
    private fun fetchWeatherData() {
        // Use Coroutines to call the Weatherbit API and update UI

        //Observe the current weather state
        lifecycleScope.launch {
            weatherViewModel.currentWeatherState.collect { state ->
                when (state) {
                    is WeatherState.Loading -> showLoadingIndicator() // Show loading indicator while fetching data
                    is WeatherState.Success -> updateUI(state.data) // Update UI with current weather data
                    is WeatherState.Error -> logError(state.message) // Log error message
                }
            }
        }

        // Observe the hourly forecast state
        lifecycleScope.launch {
            weatherViewModel.hourlyForecastState.collect { state->
                when (state) {
                    is ForecastState.Loading -> showLoadingIndicator() // Show loading indicator while fetching data
                    is ForecastState.Success -> {
                        updateHourlyForecastUI(state.data) // Update UI with hourly forecast data
                    }
                    is ForecastState.Error -> logError(state.message) // Log error message
                }
            }
        }

        // Observe the error state
        lifecycleScope.launch {
            weatherViewModel.errorState.collect { state ->
                state?.let { showError(it) } // Show error message if available
                binding.progressCircular.visibility = View.GONE // Hide loading indicator
            }
        }
    }

    /**
     * Updates the UI with the current weather data.
     *
     * @param currentWeather The response object containing the current weather data.
     */
    @SuppressLint("SetTextI18n")
    private fun updateUI(currentWeather: WeatherDataResponse) {// Update UI with current weather
        if (currentWeather.count > 0) {
            // Check if data is available
            binding.cityName.text = currentWeather.data[0].cityName // Set city name
            binding.currentTemp.text = "${currentWeather.data[0].temp}°C" // Set temperature
            binding.currentWeather.text = currentWeather.data[0].weather.description // Set weather description
            binding.localTime.text = currentWeather.data[0].datetime // Set local time
        } else {
            // Show a toast message if no data is found
            Toast.makeText(requireContext(), "No Hourly Data Found", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Updates the UI with the hourly forecast data.
     *
     * @param hourlyForecast The response object containing the hourly forecast data.
     */
    private fun updateHourlyForecastUI(hourlyForecast: HourlyForecastResponse) {
        // Update the adapter with the new data.
        adapter.updateData(hourlyForecast.data)
    }

    companion object {
        /**
         * Creates a new instance of ForecastFragment with the provided parameters.
         *
         * @param apiKey The API key for weather data.
         * @param cityName The name of the city for the forecast.
         * @param listener The listener for weather events.
         * @return A new instance of ForecastFragment.
         */
        fun newInstance(
            apiKey: String, cityName: String, listener: WeatherSDKListener
        ): ForecastFragment {
            return ForecastFragment().apply {
                this.apiKey = apiKey
                this.cityName = cityName
                this.listener = listener
            }
        }
    }

    // Shows the loading indicator (e.g., a progress bar).
    private fun showLoadingIndicator() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    // Shows an error message to the user through a listener.
    private fun showError(message: String) {
        listener.onFinishedWithError(message)
    }

    // Logs an error message to the Logcat with the tag "WeatherSDK".
    private fun logError(message: String) {
        Log.d("WeatherSDK", "logError: $message")
    }

    // Cleans up the binding reference when the view is destroyed.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
