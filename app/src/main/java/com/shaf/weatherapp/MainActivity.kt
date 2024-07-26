package com.shaf.weatherapp

import android.os.Bundle
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.android.weather.sdk.network.WeatherSDK
import com.android.weather.sdk.network.WeatherSDKImpl
import com.android.weather.sdk.network.WeatherSDKListener
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


        weatherSDK = WeatherSDKImpl().apply {
            initialize("38070488ba9f4b5f9d5b19c08b711713")
            // initialize("b0787db930684646896ac129b3413092")
            setWeatherSDKListener(this@MainActivity)
        }

        initUI(savedInstanceState)
    }

    private fun initUI(savedInstanceState: Bundle?) {
        mainbinding.contentView.btWeatherForecast.setOnClickListener {
            val cityName = mainbinding.contentView.textInputLayout.editText?.text.toString()
            if (cityName.isEmpty()) {
                mainbinding.contentView.textInputLayout.error = "Please enter a city name"
                return@setOnClickListener
            }
            // Create a new instance of the fragment
            weatherFragment = weatherSDK.launch(cityName)
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
        // Handle fragment dismissal
        Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show()
    }

    override fun onFinishedWithError(error: String) {
        fragmentManager.commit {
            setReorderingAllowed(true)
            remove(weatherFragment)
        }
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }


}