package com.bidiptoroy.weatherapp.Adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bidiptoroy.weatherapp.Model.WeatherInfo
import com.bidiptoroy.weatherapp.R

class WeatherAdapter(var context:Context,var weatherList: ArrayList<WeatherInfo>): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var txtCity: TextView = view.findViewById(R.id.txtCity)
        var txtCountry: TextView = view.findViewById(R.id.txtCountry)
        var txtTemp: TextView = view.findViewById(R.id.txtTemp)
        var txtCondition: TextView = view.findViewById(R.id.txtCondition)
//        var llConnent : LinearLayout = view.findViewById(R.id.homeContent)
        var imgWeather : ImageView = view.findViewById(R.id.imgWeather)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_home_single_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var weather = weatherList[position]
        if(weather.temp >= 20){
            holder.imgWeather.setImageResource(R.drawable.hot)
        }
        if(weather.temp<20){
            holder.imgWeather.setImageResource(R.drawable.cold)

        }
        holder.txtCity.text = weather.city
        holder.txtCountry.text = weather.country
        holder.txtCondition.text = weather.condition
        holder.txtTemp.text = weather.temp.toString() + "°C"

    }
    fun updateList(list: ArrayList<WeatherInfo>){
        weatherList.clear()
        weatherList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}