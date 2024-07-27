package com.android.weather.sdk

import android.util.Log
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// Extension function to convert ISO 8601 date-time string to 24-hour format string
fun String.to24HourFormat(): String {
    return try {
        // Define the formatter for parsing the ISO datetime string
        val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        // Parse the input string to a LocalDateTime object
        val dateTime = LocalDateTime.parse(this, isoFormatter)

        // Define the formatter for the 24-hour time format
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // Format the time part of the LocalDateTime object to a 24-hour string
        dateTime.format(timeFormatter)
    } catch (e: Exception) {
        "Invalid date format"
    }
}

// Extension function to convert timestamp to local time in 24-hour format
fun Long.toLocalTime(timezone: String): String {
    return try {
        // Create an Instant from the timestamp
        val instant = Instant.ofEpochSecond(this)

        // Get the system default timezone or specified timezone
        val zoneId = ZoneId.of(timezone)

        // Create a ZonedDateTime from the Instant
        val zonedDateTime = instant.atZone(zoneId)

        // Define the formatter for 24-hour format
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        // Return the formatted time
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        // Handle exceptions, e.g., log the error or return a default value
        Log.e("TimeConversion", "Error converting time: ${e.message}")
        "Invalid Time" // Or handle the error as needed
    }
}
