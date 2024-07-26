package com.android.weather.sdk.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.weather.sdk.data.HourlyForecastResponse
import com.android.weather.sdk.data.WeatherDataResponse
import com.android.weather.sdk.vm.ForecastState
import com.android.weather.sdk.network.WeatherSDKListener
import com.android.weather.sdk.vm.WeatherState
import com.android.weather.sdk.vm.WeatherViewModel
import com.android.weather.sdk.vm.WeatherViewModelFactory
import com.shaf.weather_sdk.databinding.FragmentForecastBinding
import kotlinx.coroutines.launch

class ForecastFragment : Fragment() {
    private lateinit var apiKey: String
    private lateinit var cityName: String
    private lateinit var listener: WeatherSDKListener

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HourlyForecastAdapter

    // Create an instance of the ViewModel using the viewModels delegate
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(cityName, apiKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView with an empty adapter
        adapter = HourlyForecastAdapter(emptyList())
        binding.hourlyForecastList.adapter = adapter

        // Fetch and display weather data
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        // Use Coroutines or another async method to call the Weatherbit API and update UI
        // Observe the current weather state
        lifecycleScope.launch {
            weatherViewModel.currentWeatherState.collect { state ->
                when (state) {
                    is WeatherState.Loading -> showLoadingIndicator()
                    is WeatherState.Success -> updateUI(state.data)
                    is WeatherState.Error -> showError(state.message)
                }
            }
        }

        // Observe the hourly forecast state
        lifecycleScope.launch {
            weatherViewModel.hourlyForecastState.collect { state ->
                when (state) {
                    is ForecastState.Loading -> showLoadingIndicator()
                    is ForecastState.Success -> {
                        updateHourlyForecastUI(state.data)
                    }

                    is ForecastState.Error -> showError(state.message)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(currentWeather: WeatherDataResponse) {
        // Update UI with current weather
        if (currentWeather.count > 0) {
            binding.cityName.text = currentWeather.data[0].cityName
            binding.currentTemp.text = "${currentWeather.data[0].temp}°C"
            binding.currentWeather.text = currentWeather.data[0].weather.description
            binding.localTime.text = currentWeather.data[0].datetime
        } else {
            Toast.makeText(requireContext(), "No Hourly Data Found", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateHourlyForecastUI(hourlyForecast: HourlyForecastResponse) {
        // Update the adapter with the new data
        adapter.updateData(hourlyForecast.data)
    }

    companion object {
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

    private fun showLoadingIndicator() {
        // Implement loading indicator display
    }

    private fun showError(message: String) {
        listener.onFinishedWithError(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
