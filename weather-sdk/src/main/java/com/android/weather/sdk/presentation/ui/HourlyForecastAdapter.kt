package com.android.weather.sdk.presentation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.weather.sdk.data.response.HourlyForecastResponse
import com.android.weather.sdk.to24HourFormat
import com.shaf.weather_sdk.R

/**
 * Adapter class for displaying hourly weather forecast data in a RecyclerView.
 *
 * @param forecastList The initial list of hourly forecast data.
 */
internal class HourlyForecastAdapter(private var forecastList: List<HourlyForecastResponse.Data>) :
    RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

    /**
     * ViewHolder class to hold references to the views in each item.
     *
     * @param view The view representing an individual item.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.time_text)
        val tempTextView: TextView = view.findViewById(R.id.temp_text)
        val descTextView: TextView = view.findViewById(R.id.desc_text)
    }

    /**
     * Creates a new ViewHolder instance.
     *
     * @param parent The parent view group.
     * @param viewType The type of the view.
     * @return A new ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly_forecast, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds data to the views in a ViewHolder.
     *
     * @param holder The ViewHolder instance.
     * @param position The position of the item in the list.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.timeTextView.text = forecast.timestampLocal.to24HourFormat()
        holder.tempTextView.text = "${forecast.temp}°C"
        holder.descTextView.text = forecast.weather.description
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the forecast list.
     */
    override fun getItemCount() = forecastList.size

    /**
     * Updates the data in the adapter using DiffUtil to calculate the differences.
     *
     * @param newForecastList The new list of hourly forecast data.
     */
    fun updateData(newForecastList: List<HourlyForecastResponse.Data>) {
        val diffCallback = ForecastDiffCallback(forecastList, newForecastList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        forecastList = newForecastList
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * DiffUtil.Callback class to calculate the differences between old and new lists.
     *
     * @param oldList The old list of hourly forecast data.
     * @param newList The new list of hourly forecast data.
     */
    class ForecastDiffCallback(
        private val oldList: List<HourlyForecastResponse.Data>,
        private val newList: List<HourlyForecastResponse.Data>
    ) : DiffUtil.Callback() {

        /**
         * Returns the size of the old list.
         *
         * @return The size of the old list.
         */
        override fun getOldListSize() = oldList.size

        /**
         * Returns the size of the new list.
         *
         * @return The size of the new list.
         */
        override fun getNewListSize() = newList.size

        /**
         * Checks if two items represent the same data.
         *
         * @param oldItemPosition The position of the item in the old list.
         * @param newItemPosition The position of the item in the new list.
         * @return True if the items represent the same data, false otherwise.
         */
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].timestampLocal == newList[newItemPosition].timestampLocal
        }

        /**
         * Checks if the contents of two items are the same.
         *
         * @param oldItemPosition The position of the item in the old list.
         * @param newItemPosition The position of the item in the new list.
         * @return True if the contents of the items are the same, false otherwise.
         */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.timestampLocal == newItem.timestampLocal &&
                    oldItem.temp == newItem.temp &&
                    oldItem.weather.description == newItem.weather.description
        }
    }
}

