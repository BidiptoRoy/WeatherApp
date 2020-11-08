package com.bidiptoroy.weatherapp.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bidiptoroy.weatherapp.Model.WeatherInfo

class WeatherWorker(context: Context, workerParams: WorkerParameters) : Worker(
        context,
        workerParams
) {

    var notification = Notification(context)
    override fun doWork(): Result {

        sendNotifications()
        return Result.success()
    }

    private fun sendNotifications() {



        val url2 = "http://api.openweathermap.org/data/2.5/weather?q=kolkata&appid=e525c6a59c9c327c7720388ee28708d2&units=metric"

        val queue = Volley.newRequestQueue(applicationContext)

            val jsonObjectRequest =object: JsonObjectRequest(Method.GET, url2, null,
                    Response.Listener {

                        Log.e("response", "success is $it")
                        val main = it.getJSONObject("main")
                        val condition = it.getJSONArray("weather")
                        val sys = it.getJSONObject("sys")

                        val w = WeatherInfo("Kolkata",
                                sys.getString("country"),
                                "100",
                                main.getDouble("temp"),
                                condition.getJSONObject(0).getString("description"),
                                main.getDouble("humidity"),
                                main.getDouble("feels_like"),
                                main.getDouble("pressure")
                        )
                        var show: String
                        var title: String
                        show = "Feels like ${w.feelsLike}°C • ${w.condition}"
                        title = w.temp.toString() + "°C " + "in Kolkata"


                        // UPDATING NOTIFICATION
                        notification.createNotification(show, title)


                    }, Response.ErrorListener {

            }){

            }
            queue.add(jsonObjectRequest)




    }

}