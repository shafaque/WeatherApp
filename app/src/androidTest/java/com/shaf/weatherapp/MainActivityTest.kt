package com.shaf.weatherapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.weather.sdk.WeatherSDK
import com.android.weather.sdk.WeatherSDKImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testWeatherForecastButton_withEmptyCityName() {
        // Launch the MainActivity
        ActivityScenario.launch<MainActivity>(MainActivity::class.java).use { scenario ->
            // Simulate button click without entering a city name
            Espresso.onView(withId(R.id.btWeatherForecast)).perform(ViewActions.click())

            // Check for error message
            Espresso.onView(withId(R.id.textInputLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun testWeatherForecastButton_withCityName() {
        // Launch the MainActivity
        ActivityScenario.launch<MainActivity>(MainActivity::class.java).use { scenario ->
            // Simulate entering a city name and clicking the button
            Espresso.onView(withId(R.id.textInputLayout))
                .perform(ViewActions.replaceText("Berlin"))
            Espresso.onView(withId(R.id.btWeatherForecast)).perform(ViewActions.click())

            // Verify that the fragment is displayed
            Espresso.onView(withId(R.id.fragment_container_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }
}