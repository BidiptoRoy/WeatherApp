package com.bidiptoroy.weatherapp.Model

data class WeatherInfo(var city:String,
                       var country: String,
                       var localTime: String,
                       var temp: Double,
                       var condition:String,
//                       var icon: String,
                       var humidity: Double,
                       var feelsLike: Double,
                       var pressure: Double) {
}