package com.android.weather.sdk.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.weather.sdk.WeatherSDKListener
import com.android.weather.sdk.data.response.HourlyForecastResponse
import com.android.weather.sdk.data.response.WeatherDataResponse
import com.android.weather.sdk.presentation.vm.ForecastState
import com.android.weather.sdk.presentation.vm.WeatherState
import com.android.weather.sdk.presentation.vm.WeatherViewModel
import com.android.weather.sdk.toLocalTime
import com.shaf.weather_sdk.R
import com.shaf.weather_sdk.databinding.FragmentForecastBinding
import kotlinx.coroutines.launch

/**
 * A Fragment that displays the weather forecast for a given city.
 *
 * This fragment interacts with the ViewModel to fetch weather data and displays it to the user.
 * It also allows back navigation and reports errors via [WeatherSDKListener].
 */
class ForecastFragment : Fragment() {

    // API key and city name needed to fetch weather data, passed via arguments
    private lateinit var apiKey: String
    private lateinit var cityName: String
    private lateinit var listener: WeatherSDKListener

    // View binding to handle the layout views
    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    // Adapter for the hourly forecast RecyclerView
    private lateinit var forecastAdapter: HourlyForecastAdapter

    // ViewModel for weather data, using viewModels() to instantiate the ViewModel
    private val weatherViewModel: WeatherViewModel by viewModels()

    /**
     * Called when the Fragment is created. Retrieves the cityName and apiKey from the arguments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the apiKey and cityName from the Fragment arguments
        arguments?.let {
            apiKey = it.getString("apiKey").orEmpty()
            cityName = it.getString("cityName").orEmpty()
        }
    }

    /**
     * Inflates the layout for the fragment and returns the root view.
     *
     * Adds custom back navigation behavior through [WeatherSDKListener].
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)

        // Set up a callback for handling the back press and interacting with the listener
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            listener.onFinished()
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Trigger the ViewModel to fetch weather data using cityName and apiKey
        weatherViewModel.fetchWeatherData(cityName, apiKey)

        return binding.root
    }

    /**
     * Sets up the UI elements like RecyclerView and action bar after the view is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure action bar title and back navigation
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.toolbar_title)
        }

        // Set up the RecyclerView to display hourly forecasts
        setRecyclerView()

        // Start observing the weather data from ViewModel and update UI accordingly
        fetchWeatherData()
    }

    /**
     * Configures the RecyclerView to display the hourly forecast.
     *
     * Sets a vertical layout, adds item dividers, and initializes the forecast adapter.
     */
    private fun setRecyclerView() {
        // Initialize the adapter with an empty list
        forecastAdapter = HourlyForecastAdapter(emptyList())

        // Create a vertical LinearLayoutManager for displaying items
        val linearLayoutManager = LinearLayoutManager(
            binding.hourlyForecastList.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        // Add a divider line between items
        val dividerItemDecoration = DividerItemDecoration(
            binding.hourlyForecastList.context,
            linearLayoutManager.orientation
        )

        // Apply the layout manager and adapter to the RecyclerView
        binding.hourlyForecastList.apply {
            layoutManager = linearLayoutManager
            adapter = forecastAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }

    /**
     * Observes the weather and forecast states from the ViewModel and updates the UI.
     *
     * Weather and forecast data is fetched and displayed, with error handling.
     */
    private fun fetchWeatherData() {
        // Observe current weather state and update the UI
        lifecycleScope.launch {
            weatherViewModel.currentWeatherState.collect { state ->
                when (state) {
                    is WeatherState.Loading -> showLoadingIndicator() // Show loading indicator
                    is WeatherState.Success -> updateUI(state.data)   // Update UI with weather data
                    is WeatherState.Error -> logError(state.message)  // Log any error
                }
            }
        }

        // Observe hourly forecast state and update the RecyclerView
        lifecycleScope.launch {
            weatherViewModel.hourlyForecastState.collect { state ->
                when (state) {
                    is ForecastState.Loading -> showLoadingIndicator() // Show loading indicator
                    is ForecastState.Success -> updateHourlyForecastUI(state.data) // Update UI with forecast data
                    is ForecastState.Error -> logError(state.message) // Log any error
                }
            }
        }

        // Observe any error states and display error messages
        lifecycleScope.launch {
            weatherViewModel.errorState.collect { state ->
                state?.let { showError(it) } // Show error message if available
                binding.progressCircular.visibility = View.GONE // Hide loading indicator
            }
        }
    }

    /**
     * Updates the UI with current weather data received from the ViewModel.
     *
     * @param currentWeather The response containing current weather data.
     */
    @SuppressLint("SetTextI18n")
    private fun updateUI(currentWeather: WeatherDataResponse) {
        if (currentWeather.count > 0) {
            // Update city name, temperature, and weather description
            binding.cityName.text = currentWeather.data[0].cityName
            binding.currentTemp.text = "${currentWeather.data[0].temp}°C"
            binding.currentWeather.text = currentWeather.data[0].weather.description

            // Convert and display the local time
            val localTime = currentWeather.data[0].ts.toLocalTime(currentWeather.data[0].timezone)
            val resultString = resources.getString(R.string.local_time_format, localTime)
            binding.localTime.text = resultString
        } else {
            Toast.makeText(requireContext(), "No Hourly Data Found", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Updates the RecyclerView with hourly forecast data.
     *
     * @param hourlyForecast The response containing hourly forecast data.
     */
    private fun updateHourlyForecastUI(hourlyForecast: HourlyForecastResponse) {
        // Hide the loading indicator and update the adapter with forecast data
        binding.progressCircular.visibility = View.GONE
        forecastAdapter.updateData(hourlyForecast.data)
    }

    companion object {
        /**
         * Creates a new instance of ForecastFragment with the provided API key and city name.
         *
         * @param apiKey The API key for accessing weather data.
         * @param cityName The name of the city for which the forecast is displayed.
         * @param listener The listener for handling weather SDK events.
         * @return A new instance of ForecastFragment.
         */
        fun newInstance(
            apiKey: String,
            cityName: String,
            listener: WeatherSDKListener,
        ): ForecastFragment {
            return ForecastFragment().apply {
                arguments = Bundle().apply {
                    putString("apiKey", apiKey)
                    putString("cityName", cityName)
                }
                this.listener = listener
            }
        }
    }

    /**
     * Shows a loading indicator (e.g., progress bar).
     */
    private fun showLoadingIndicator() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    /**
     * Shows an error message via the listener.
     *
     * @param message The error message to be displayed.
     */
    private fun showError(message: String) {
        listener.onFinishedWithError(message)
    }

    /**
     * Logs an error message using Logcat.
     *
     * @param message The error message to log.
     */
    private fun logError(message: String) {
        Log.d("WeatherSDK", "logError: $message")
    }

    /**
     * Cleans up view binding when the fragment's view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
