package com.android.weather.sdk.data

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * Represents the response from a weather data API.
 *
 * @property count The number of data points returned.
 * @property data The list of weather data points.
 */
@Keep
data class WeatherDataResponse(
    @SerializedName("count")
    val count: Int, // The number of data points returned.
    @SerializedName("data")
    val data: List<Data> // The list of weather data points.
) {
    /**
     * Represents a single weather data point.
     *
     * @property appTemp The apparent temperature in Celsius.
     * @property aqi The Air Quality Index (AQI).
     * @property cityName The name of the city.
     * @property clouds The percentage of cloud coverage.
     * @property countryCode The ISO 3166-1 alpha-2 code of the country.
     * @property datetime The date and time of the observation in ISO 8601 format.
     * @property dewpt The dew point temperature in Celsius.
     * @property dhi Diffuse Horizontal Irradiance in watts per square meter.
     * @property dni Direct Normal Irradiance in watts per square meter.
     * @property elevAngle The solar elevation angle in degrees.
     * @property ghi Global Horizontal Irradiance in watts per square meter.
     * @property gust The wind gust speed in meters per second.
     * @property hAngle The hour angle of the sun in degrees.
     * @property lat The latitude of the location.
     * @property lon The longitude of the location.
     * @property obTime The observation time in UTC.
     * @property pod The part of the day ("d" for day, "n" for night).
     * @property precip The precipitation amount in millimeters.
     * @property pres The atmospheric pressure in hPa (hectopascals).
     * @property rh The relative humidity as a percentage.
     * @property slp The sea level pressure in hPa.
     * @property snow The snow amount in millimeters.
     * @property solarRad The solar radiation in watts per square meter.
     * @property sources The list of sources for the weather data.
     * @property stateCode The state code where the observation is made.
     * @property station The weather station ID.
     * @property sunrise The time of sunrise in local time.
     * @property sunset The time of sunset in local time.
     * @property temp The current temperature in Celsius.
     * @property timezone The timezone of the location.
     * @property ts The Unix timestamp of the observation.
     * @property uv The UV index.
     * @property vis The visibility distance in kilometers.
     * @property weather The weather conditions.
     * @property windCdir The abbreviated wind direction.
     * @property windCdirFull The full wind direction.
     * @property windDir The wind direction in degrees.
     * @property windSpd The wind speed in meters per second.
     */
    @Keep
    data class Data(
        @SerializedName("app_temp")
        val appTemp: Double, // The apparent temperature in Celsius.
        @SerializedName("aqi")
        val aqi: Int, // The Air Quality Index (AQI).
        @SerializedName("city_name")
        val cityName: String, // The name of the city.
        @SerializedName("clouds")
        val clouds: Int, // The percentage of cloud coverage.
        @SerializedName("country_code")
        val countryCode: String, // The ISO 3166-1 alpha-2 code of the country.
        @SerializedName("datetime")
        val datetime: String, // The date and time of the observation in ISO 8601 format.
        @SerializedName("dewpt")
        val dewpt: Double, // The dew point temperature in Celsius.
        @SerializedName("dhi")
        val dhi: Int, // Diffuse Horizontal Irradiance in watts per square meter.
        @SerializedName("dni")
        val dni: Int, // Direct Normal Irradiance in watts per square meter.
        @SerializedName("elev_angle")
        val elevAngle: Double, // The solar elevation angle in degrees.
        @SerializedName("ghi")
        val ghi: Int, // Global Horizontal Irradiance in watts per square meter.
        @SerializedName("gust")
        val gust: Double, // The wind gust speed in meters per second.
        @SerializedName("h_angle")
        val hAngle: Double, // The hour angle of the sun in degrees.
        @SerializedName("lat")
        val lat: Double, // The latitude of the location.
        @SerializedName("lon")
        val lon: Double, // The longitude of the location.
        @SerializedName("ob_time")
        val obTime: String, // The observation time in UTC.
        @SerializedName("pod")
        val pod: String, // The part of the day ("d" for day, "n" for night).
        @SerializedName("precip")
        val precip: Double, // The precipitation amount in millimeters.
        @SerializedName("pres")
        val pres: Double, // The atmospheric pressure in hPa (hectopascals).
        @SerializedName("rh")
        val rh: Int, // The relative humidity as a percentage.
        @SerializedName("slp")
        val slp: Double, // The sea level pressure in hPa.
        @SerializedName("snow")
        val snow: Int, // The snow amount in millimeters.
        @SerializedName("solar_rad")
        val solarRad: Double, // The solar radiation in watts per square meter.
        @SerializedName("sources")
        val sources: List<String>, // The list of sources for the weather data.
        @SerializedName("state_code")
        val stateCode: String, // The state code where the observation is made.
        @SerializedName("station")
        val station: String, // The weather station ID.
        @SerializedName("sunrise")
        val sunrise: String, // The time of sunrise in local time.
        @SerializedName("sunset")
        val sunset: String, // The time of sunset in local time.
        @SerializedName("temp")
        val temp: Double, // The current temperature in Celsius.
        @SerializedName("timezone")
        val timezone: String, // The timezone of the location.
        @SerializedName("ts")
        val ts: Long, // The Unix timestamp of the observation.
        @SerializedName("uv")
        val uv: Double, // The UV index.
        @SerializedName("vis")
        val vis: Double, // The visibility distance in kilometers.
        @SerializedName("weather")
        val weather: Weather, // The weather conditions.
        @SerializedName("wind_cdir")
        val windCdir: String, // The abbreviated wind direction.
        @SerializedName("wind_cdir_full")
        val windCdirFull: String, // The full wind direction.
        @SerializedName("wind_dir")
        val windDir: Int, // The wind direction in degrees.
        @SerializedName("wind_spd")
        val windSpd: Double // The wind speed in meters per second.
    ) {
        /**
         * Represents the weather conditions.
         *
         * @property code The weather condition code.
         * @property description The description of the weather condition.
         * @property icon The icon representing the weather condition.
         */
        @Keep
        data class Weather(
            @SerializedName("code")
            val code: Int, // The weather condition code.
            @SerializedName("description")
            val description: String, // The description of the weather condition.
            @SerializedName("icon")
            val icon: String // The icon representing the weather condition.
        )
    }
}
