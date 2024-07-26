package com.android.weather.sdk

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// Extension function to convert ISO 8601 date-time string to 24-hour format string
fun String.to24HourFormat(): String {
    return try {
        // Define the formatter for parsing the ISO datetime string
        val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        // Parse the input string to a LocalDateTime object
        val dateTime = LocalDateTime.parse(this, isoFormatter)

        // Define the formatter for the 24-hour time format
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        // Format the time part of the LocalDateTime object to a 24-hour string
        dateTime.format(timeFormatter)
    } catch (e: Exception) {
        "Invalid date format"
    }
}
// Extension function to convert a string in 'yyyy-MM-dd:HH' format to a 24-hour time string
fun String.to24HourFormatWithHourOnly(): String {
    return try {
        // Define a custom formatter for the input date-time pattern
        val customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH")

        // Parse the input string to a LocalDateTime object using the custom pattern
        val dateTime = LocalDateTime.parse(this, customFormatter)

        // Define the formatter for the 24-hour time format
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        // Format the time part of the LocalDateTime object to a 24-hour string
        dateTime.format(timeFormatter)
    } catch (e: Exception) {
        "Invalid date format"
    }
}
