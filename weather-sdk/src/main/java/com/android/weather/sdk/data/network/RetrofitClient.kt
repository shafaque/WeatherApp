package com.android.weather.sdk.data.network

import com.android.weather.sdk.data.WebServices
import com.google.gson.GsonBuilder
import com.shaf.weather_sdk.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object for managing Retrofit client and network requests.
 */
object RetrofitClient {
    // An instance of WebServices interface for API calls
    private val webServices: WebServices

    // OkHttpClient to handle HTTP requests
    private val okHttpClient: OkHttpClient

    // Base URL for the WeatherBit API
    private const val BASE_URL = "https://api.weatherbit.io/v2.0/"

    init {
        // Initialize OkHttpClient.Builder to configure the OkHttpClient
        val okHttpBuilder = OkHttpClient.Builder()

        // Enable logging of HTTP requests and responses in debug builds
        if (BuildConfig.DEBUG) {
            // Create an interceptor for logging HTTP request/response data
            val logging = HttpLoggingInterceptor()
            // Set the logging level to BODY to log request and response bodies
            logging.level = HttpLoggingInterceptor.Level.BODY
            // Add the logging interceptor to OkHttpClient
            okHttpBuilder.addInterceptor(logging)
        }

        // Configure OkHttpClient with timeout settings
        okHttpClient = okHttpBuilder
            .readTimeout(60, TimeUnit.SECONDS)    // Read timeout: 60 seconds
            .writeTimeout(60, TimeUnit.SECONDS)   // Write timeout: 60 seconds
            .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout: 60 seconds
            .build()                              // Build the OkHttpClient instance

        // Initialize GsonBuilder for JSON serialization/deserialization
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create() // Create Gson instance from the builder

        // Configure Retrofit with base URL, Gson converter, and OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)                       // Set the base URL for the API
            .addConverterFactory(GsonConverterFactory.create(gson)) // Add Gson converter for JSON
            .client(okHttpClient)                    // Attach the OkHttpClient
            .build()                                 // Build the Retrofit instance

        // Create an instance of WebServices using Retrofit
        webServices = retrofit.create(WebServices::class.java)
    }

    /**
     * Returns the WebServices instance for API calls.
     *
     * @return An instance of WebServices for making API requests.
     */
    internal fun getService(): WebServices = webServices
}
