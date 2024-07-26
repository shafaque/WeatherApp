Here is the revised documentation for WeatherSDK with the correct installation instructions for importing it as a project module:

---

# WeatherSDK Android

WeatherSDK is a simple and powerful Android library designed to fetch weather data seamlessly. It provides developers with an easy-to-integrate solution for retrieving weather information in Android applications.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Configuration](#configuration)
- [License](#license)
- [Contact](#contact)

## Features

- **Easy Integration**: Quickly integrate the library into any Android project.
- **Real-Time Weather Data**: Fetches current weather conditions and forecasts.
- **Customizable**: Offers options to customize the data retrieval and display.
- **Lightweight**: Minimal impact on the app’s performance.

## Installation

### Import as Project Module

1. Download or clone the WeatherSDK project.
2. Open your Android project in Android Studio.
3. Go to `File > New > Import Module`.
4. Select the WeatherSDK directory and click `Finish`.
5. Open your app's `build.gradle` file and add the following line to the `dependencies` block:

```gradle
implementation project(':weather-sdk')
```

## Getting Started

Here's a simple guide to get you started with WeatherSDK.

### Step 1: Initialize the Library

Before using the library, initialize it in your `Application` class or `Activity`:

```kotlin
import com.android.weather.sdk.WeatherSDK
import com.android.weather.sdk.WeatherSDKImpl

val weatherSDK = WeatherSDKImpl().apply {
    initialize("YOUR_API_KEY")
    setWeatherSDKListener(this@YourActivity)
}
```

### Step 2: Request Permissions

Ensure that you have the necessary permissions in your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Step 3: Fetch Weather Data

Use the library to fetch weather data by following the usage instructions below.

## Usage

### Fetch Weather by City Name

You can fetch weather data by city name and display it in a fragment:

```kotlin
import com.android.weather.sdk.WeatherSDK
import com.android.weather.sdk.WeatherSDKImpl
import androidx.fragment.app.commit

val weatherSDK = WeatherSDKImpl().apply {
    initialize("YOUR_API_KEY")
    setWeatherSDKListener(this@YourActivity)
}

val cityName = "Berlin"
val weatherFragment = weatherSDK.launch(cityName)
if (savedInstanceState == null) {
    // Add the fragment to the container
    supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace(R.id.fragmentContainerView, weatherFragment)
    }
}
```

## Configuration

WeatherSDK supports several configuration options:

### API Key

Ensure you have your API key ready for initialization. Replace `"YOUR_API_KEY"` with your actual API key when initializing WeatherSDK:

```kotlin
val weatherSDK = WeatherSDKImpl().apply {
    initialize("YOUR_API_KEY")
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any inquiries or support, please contact us at [shafaquesattar@hotmail.com](mailto:shafaquesattar@hotmail.com).

---

This version correctly describes how to import WeatherSDK as a project module and ensures consistency throughout the documentation.