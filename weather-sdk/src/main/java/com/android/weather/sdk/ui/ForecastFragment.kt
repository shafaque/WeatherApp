package com.android.weather.sdk.ui

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
import com.android.weather.sdk.data.HourlyForecastResponse
import com.android.weather.sdk.data.WeatherDataResponse
import com.android.weather.sdk.toLocalTime
import com.android.weather.sdk.vm.ForecastState
import com.android.weather.sdk.vm.WeatherState
import com.android.weather.sdk.vm.WeatherViewModel
import com.android.weather.sdk.vm.WeatherViewModelFactory
import com.shaf.weather_sdk.R
import com.shaf.weather_sdk.databinding.FragmentForecastBinding
import kotlinx.coroutines.launch


/**
 * A Fragment that displays the weather forecast for a given city.
 */
class ForecastFragment : Fragment() {
    // Declare private properties for API key, city name, and listener
    private lateinit var apiKey: String
    private lateinit var cityName: String
    private lateinit var listener: WeatherSDKListener

    // View binding for the fragment's layout
    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    // Adapter for the hourly forecast RecyclerView
    private lateinit var forecastAdapter: HourlyForecastAdapter

    // ViewModel instance using delegated property with a custom factory
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(cityName, apiKey)
    }

    /**
     * Inflates the fragment's layout and returns the root view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Custom back press behavior
            listener.onFinished()

            // Pop the back stack to go back to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
        return binding.root
    }

    /**
     * Sets up UI elements and fetches weather data after the view is created.
     *
     * @param view The view returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.toolbar_title)

        }
        // Set up RecyclerView
        setRecyclerView()

        // Fetch and display weather data
        fetchWeatherData()

    }

    /**
     * Configures the RecyclerView to display a list of hourly weather forecasts.
     *
     * This function initializes the RecyclerView with an empty adapter, sets up
     * a vertical `LinearLayoutManager`, adds a divider between each item for visual
     * separation, and applies these configurations to the RecyclerView. This is
     * particularly useful when displaying weather data in a structured list format.
     */
    private fun setRecyclerView() {
        // Initialize the RecyclerView with an empty adapter
        forecastAdapter = HourlyForecastAdapter(emptyList())

        // Create a LinearLayoutManager for arranging items vertically
        val linearLayoutManager = LinearLayoutManager(
            binding.hourlyForecastList.context, // Context from the binding
            LinearLayoutManager.VERTICAL,       // Orientation set to vertical
            false                               // Reverse layout disabled
        )

        // Add a divider line between items in the RecyclerView
        val dividerItemDecoration = DividerItemDecoration(
            binding.hourlyForecastList.context, // Context from the binding
            linearLayoutManager.orientation     // Orientation of the divider
        )

        // Apply layout manager, adapter, and item decoration to the RecyclerView
        binding.hourlyForecastList.apply {
            layoutManager = linearLayoutManager  // Set the layout manager
            adapter = forecastAdapter            // Assign the adapter
            addItemDecoration(dividerItemDecoration) // Add item decoration
        }
    }


    /**
     * Fetches weather data and updates the UI.
     */
    private fun fetchWeatherData() {
        // Use Coroutines to call the Weatherbit API and update UI

        // Observe the current weather state
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
            weatherViewModel.hourlyForecastState.collect { state ->
                when (state) {
                    is ForecastState.Loading -> showLoadingIndicator() // Show loading indicator while fetching data
                    is ForecastState.Success -> updateHourlyForecastUI(state.data) // Update UI with hourly forecast data
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
    private fun updateUI(currentWeather: WeatherDataResponse) {
        // Update UI with current weather
        if (currentWeather.count > 0) {
            // Check if data is available
            binding.cityName.text = currentWeather.data[0].cityName // Set city name
            binding.currentTemp.text = "${currentWeather.data[0].temp}°C" // Set temperature
            binding.currentWeather.text =
                currentWeather.data[0].weather.description // Set weather description

            val localTime = currentWeather.data[0].ts.toLocalTime(currentWeather.data[0].timezone)
            val resultString = resources.getString(R.string.local_time_format, localTime)
            binding.localTime.text = resultString // Set local time
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

        // Hide loading indicator
        binding.progressCircular.visibility = View.GONE

        // Update the adapter with the new data.
        forecastAdapter.updateData(hourlyForecast.data)
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

    /**
     * Shows the loading indicator (e.g., a progress bar).
     */
    private fun showLoadingIndicator() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    /**
     * Shows an error message to the user through a listener.
     *
     * @param message The error message to be displayed.
     */
    private fun showError(message: String) {
        listener.onFinishedWithError(message)
    }

    /**
     * Logs an error message to the Logcat with the tag "WeatherSDK".
     *
     * @param message The error message to be logged.
     */
    private fun logError(message: String) {
        Log.d("WeatherSDK", "logError: $message")
    }

    /**
     * Cleans up the binding reference when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
