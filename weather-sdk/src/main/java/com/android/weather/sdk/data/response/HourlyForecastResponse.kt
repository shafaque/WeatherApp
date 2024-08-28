package com.android.weather.sdk.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the hourly forecast response.
 *
 * @property cityName Name of the city.
 * @property countryCode Code of the country.
 * @property data List of hourly data.
 * @property lat Latitude of the location.
 * @property lon Longitude of the location.
 * @property stateCode Code of the state.
 * @property timezone Timezone of the location.
 */
@Keep
internal data class HourlyForecastResponse(
    /** The name of the city for which the forecast is provided */
    @SerializedName("city_name")
    val cityName: String,

    /** The ISO country code for the country where the city is located */
    @SerializedName("country_code")
    val countryCode: String,

    /** A list of hourly forecast data */
    @SerializedName("data")
    val data: List<Data>,

    /** The latitude of the city's location */
    @SerializedName("lat")
    val lat: String,

    /** The longitude of the city's location */
    @SerializedName("lon")
    val lon: String,

    /** The code for the state where the city is located */
    @SerializedName("state_code")
    val stateCode: String,

    /** The timezone of the city's location */
    @SerializedName("timezone")
    val timezone: String
) {
    /**
     * Data class representing the hourly forecast data.
     *
     * @property appTemp Apparent temperature.
     * @property clouds Total cloud coverage percentage.
     * @property cloudsHi High-level cloud coverage percentage.
     * @property cloudsLow Low-level cloud coverage percentage.
     * @property cloudsMid Mid-level cloud coverage percentage.
     * @property datetime Date and time of the forecast.
     * @property dewpt Dew point temperature.
     * @property dhi Diffuse horizontal irradiance.
     * @property dni Direct normal irradiance.
     * @property ghi Global horizontal irradiance.
     * @property ozone Ozone concentration.
     * @property pod Part of the day (day/night).
     * @property pop Probability of precipitation.
     * @property precip Precipitation amount.
     * @property pres Pressure.
     * @property rh Relative humidity.
     * @property slp Sea level pressure.
     * @property snow Snowfall amount.
     * @property snowDepth Snow depth.
     * @property solarRad Solar radiation.
     * @property temp Temperature.
     * @property timestampLocal Local timestamp.
     * @property timestampUtc UTC timestamp.
     * @property ts Unix timestamp.
     * @property uv UV index.
     * @property vis Visibility distance.
     * @property weather Weather conditions.
     * @property windCdir Wind direction in compass format.
     * @property windCdirFull Full wind direction description.
     * @property windDir Wind direction in degrees.
     * @property windGustSpd Wind gust speed.
     * @property windSpd Wind speed.
     */
    @Keep
    data class Data(
        /** The apparent temperature (feels like) in degrees Celsius */
        @SerializedName("app_temp")
        val appTemp: Double,

        /** The total cloud coverage as a percentage */
        @SerializedName("clouds")
        val clouds: Int,

        /** The high-level cloud coverage as a percentage */
        @SerializedName("clouds_hi")
        val cloudsHi: Int,

        /** The low-level cloud coverage as a percentage */
        @SerializedName("clouds_low")
        val cloudsLow: Int,

        /** The mid-level cloud coverage as a percentage */
        @SerializedName("clouds_mid")
        val cloudsMid: Int,

        /** The local date and time of the forecast */
        @SerializedName("datetime")
        val datetime: String,

        /** The dew point temperature in degrees Celsius */
        @SerializedName("dewpt")
        val dewpt: Double,

        /** Diffuse horizontal irradiance in W/m² */
        @SerializedName("dhi")
        val dhi: Int,

        /** Direct normal irradiance in W/m² */
        @SerializedName("dni")
        val dni: Int,

        /** Global horizontal irradiance in W/m² */
        @SerializedName("ghi")
        val ghi: Int,

        /** Ozone concentration in Dobson units */
        @SerializedName("ozone")
        val ozone: Int,

        /** Part of the day (day/night) indicator */
        @SerializedName("pod")
        val pod: String,

        /** Probability of precipitation as a percentage */
        @SerializedName("pop")
        val pop: Int,

        /** Precipitation amount in millimeters */
        @SerializedName("precip")
        val precip: Double,

        /** Atmospheric pressure in hPa */
        @SerializedName("pres")
        val pres: Double,

        /** Relative humidity as a percentage */
        @SerializedName("rh")
        val rh: Int,

        /** Sea level pressure in hPa */
        @SerializedName("slp")
        val slp: Int,

        /** Snowfall amount in millimeters */
        @SerializedName("snow")
        val snow: Int,

        /** Snow depth in millimeters */
        @SerializedName("snow_depth")
        val snowDepth: Int,

        /** Solar radiation in W/m² */
        @SerializedName("solar_rad")
        val solarRad: Double,

        /** Temperature in degrees Celsius */
        @SerializedName("temp")
        val temp: Double,

        /** Local timestamp in ISO 8601 format */
        @SerializedName("timestamp_local")
        val timestampLocal: String,

        /** UTC timestamp in ISO 8601 format */
        @SerializedName("timestamp_utc")
        val timestampUtc: String,

        /** Unix timestamp in seconds */
        @SerializedName("ts")
        val ts: Int,

        /** UV index */
        @SerializedName("uv")
        val uv: Int,

        /** Visibility distance in kilometers */
        @SerializedName("vis")
        val vis: Double,

        /** Weather conditions represented by a nested Weather object */
        @SerializedName("weather")
        val weather: Weather,

        /** Wind direction in compass format (e.g., N, NE, E, etc.) */
        @SerializedName("wind_cdir")
        val windCdir: String,

        /** Full wind direction description (e.g., North, North-East, etc.) */
        @SerializedName("wind_cdir_full")
        val windCdirFull: String,

        /** Wind direction in degrees */
        @SerializedName("wind_dir")
        val windDir: Int,

        /** Wind gust speed in meters per second */
        @SerializedName("wind_gust_spd")
        val windGustSpd: Double,

        /** Wind speed in meters per second */
        @SerializedName("wind_spd")
        val windSpd: Double
    ) {
        /**
         * Data class representing the weather conditions.
         *
         * @property code Weather condition code.
         * @property description Description of the weather condition.
         * @property icon Icon representing the weather condition.
         */
        @Keep
        data class Weather(
            /** Weather condition code (e.g., 800 for clear sky) */
            @SerializedName("code")
            val code: Int,

            /** Description of the weather condition (e.g., clear sky, few clouds) */
            @SerializedName("description")
            val description: String,

            /** Icon code for the weather condition (e.g., "01d" for clear sky day) */
            @SerializedName("icon")
            val icon: String
        )
    }
}
