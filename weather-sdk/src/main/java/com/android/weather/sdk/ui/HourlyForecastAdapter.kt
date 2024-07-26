package com.android.weather.sdk.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.weather.sdk.data.HourlyForecastResponse
import com.shaf.weather_sdk.R

 class HourlyForecastAdapter(private var forecastList: List<HourlyForecastResponse.Data>) :
    RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

    // ViewHolder class to hold references to the views in each item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.time_text)
        val tempTextView: TextView = view.findViewById(R.id.temp_text)
        val descTextView: TextView = view.findViewById(R.id.desc_text)
    }

    // Create a new ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly_forecast, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the views in a ViewHolder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.timeTextView.text = forecast.timestampLocal
        holder.tempTextView.text = "${forecast.temp}°C"
        holder.descTextView.text = forecast.weather.description
    }

    // Return the total number of items in the list
    override fun getItemCount() = forecastList.size

    // Update the data in the adapter using DiffUtil
    fun updateData(newForecastList: List<HourlyForecastResponse.Data>) {
        val diffCallback = ForecastDiffCallback(forecastList, newForecastList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        forecastList = newForecastList
        diffResult.dispatchUpdatesTo(this)
    }

    // DiffUtil.Callback class to calculate the differences between old and new lists
    class ForecastDiffCallback(
        private val oldList: List<HourlyForecastResponse.Data>,
        private val newList: List<HourlyForecastResponse.Data>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].timestampLocal == newList[newItemPosition].timestampLocal
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.timestampLocal == newItem.timestampLocal &&
                    oldItem.temp == newItem.temp &&
                    oldItem.weather.description == newItem.weather.description
        }
    }
}

