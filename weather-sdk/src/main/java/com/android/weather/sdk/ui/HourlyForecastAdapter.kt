package com.android.weather.sdk.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.weather.sdk.data.HourlyForecastResponse
import com.shaf.weather_sdk.R

class HourlyForecastAdapter(private var forecastList: List<HourlyForecastResponse.Data>) : RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.time_text)
        val tempTextView: TextView = view.findViewById(R.id.temp_text)
        val descTextView: TextView = view.findViewById(R.id.desc_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly_forecast, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.timeTextView.text = forecast.timestampLocal
        holder.tempTextView.text = "${forecast.temp}°C"
        holder.descTextView.text = forecast.weather.description
    }

    override fun getItemCount() = forecastList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newForecastList: List<HourlyForecastResponse.Data>) {
        forecastList = newForecastList
        notifyDataSetChanged()
    }
}
