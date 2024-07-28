package com.shaf.weatherapp

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.android.weather.sdk.WeatherSDK
import com.android.weather.sdk.WeatherSDKImpl
import com.android.weather.sdk.WeatherSDKListener
import com.shaf.weatherapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), WeatherSDKListener {
    private lateinit var weatherFragment: Fragment
    private lateinit var weatherSDK: WeatherSDK
    private lateinit var mainbinding: ActivityMainBinding

    // Begin a fragment transaction
    private val fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainbinding.root)
        setSupportActionBar(mainbinding.toolbar)
        // Enable the back button in the toolbar
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.title = getString(R.string.example_app) // Set the toolbar title
        }

        mainbinding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        weatherSDK = WeatherSDKImpl().apply {
            initialize(BuildConfig.API_KEY)
            setWeatherSDKListener(this@MainActivity)
        }

        initUI(savedInstanceState)
    }


    private fun initUI(savedInstanceState: Bundle?) {
        mainbinding.contentView.btWeatherForecast.setOnClickListener {
            val cityName = mainbinding.contentView.textInputLayout.editText?.text.toString()
            if (cityName.isEmpty()) {
                mainbinding.contentView.textInputLayout.error =
                    getString(R.string.please_enter_a_city_name)
                return@setOnClickListener
            }
            // Create a new instance of the fragment
            weatherFragment = weatherSDK.launch(cityName)
            // reset the input field
            mainbinding.contentView.textInputLayout.editText?.text?.clear()
            mainbinding.contentView.textInputLayout.error = null
            hideKeyboard()

            // Check if the fragment container is already filled
            if (savedInstanceState == null) {
                // Add the fragment to the container
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(mainbinding.contentView.fragmentContainerView.id, weatherFragment)
                }
            }

        }
    }

    override fun onFinished() {
        resetUI()
        Toast.makeText(this, getString(R.string.weather_details_dismissed), Toast.LENGTH_SHORT)
            .show()
    }


    override fun onFinishedWithError(error: String) {
        resetUI()
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun resetUI() {
        fragmentManager.commit {
            setReorderingAllowed(true)
            remove(weatherFragment)
        }
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.title = getString(R.string.example_app) // Set the toolbar title
        }
    }

    private fun hideKeyboard() {
        val view = currentFocus
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }


}