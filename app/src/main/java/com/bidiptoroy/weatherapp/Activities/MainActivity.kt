package com.bidiptoroy.weatherapp.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bidiptoroy.weatherapp.Adapter.WeatherAdapter
import com.bidiptoroy.weatherapp.Model.WeatherInfo
import com.bidiptoroy.weatherapp.R
import com.bidiptoroy.weatherapp.util.ConnectionManager
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var homeRecyclerView: RecyclerView
    lateinit var homeRecyclerAdapter : WeatherAdapter
    lateinit var linearLayoutManager: RecyclerView.LayoutManager
     var list = arrayListOf<WeatherInfo>()
lateinit var searchButton :SearchView
    var city : String = "Kolkata"
    var lat: Double = 22.5726
    var lon: Double = 88.3639
    lateinit var txtHomeCity: TextView
    lateinit var txtHomeCountry: TextView
    lateinit var txtHomeTemp: TextView
    lateinit var txtHomeCondition: TextView
var count =5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.search_button)



//        if (searchButton.query != null) city = searchButton.query.toString()
//        setUpHome(city)

        searchButton.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
// do something on text submit
//                setUpHome(searchButton.query.toString())
                city = searchButton.query.toString()
                list.clear()
                setUpHome(city)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
// do something when text changes

                city = searchButton.query.toString()
                list.clear()
                setUpHome(city)

                return false
            }
        })

        homeRecyclerView =findViewById(R.id.recyclerHome)
        linearLayoutManager= LinearLayoutManager(this)
        homeRecyclerView.layoutManager = linearLayoutManager
        fetchData(lat,lon, count)
        homeRecyclerAdapter = WeatherAdapter(this,list)
        homeRecyclerView.itemAnimator = DefaultItemAnimator()
        homeRecyclerView.adapter = homeRecyclerAdapter
    }

    private fun setUpHome(city: String) {

        txtHomeCity = findViewById(R.id.homeCity)
        txtHomeCountry = findViewById(R.id.homeCountry)
        txtHomeCondition = findViewById(R.id.homeCondition)
        txtHomeTemp = findViewById(R.id.homeTemp)

        val url2 = "http://api.openweathermap.org/data/2.5/weather?q=$city&appid=e525c6a59c9c327c7720388ee28708d2&units=metric"
        val queue = Volley.newRequestQueue(this)
        if(ConnectionManager().checkConnectivity(this)){

            val jsonObjectRequest =object: JsonObjectRequest(Method.GET, url2, null,
                    Response.Listener {

                        Log.e("response", "success is $it")
                        val main = it.getJSONObject("main")
                        val condition = it.getJSONArray("weather")
                        val sys = it.getJSONObject("sys")

                        val w = WeatherInfo(city,
                                sys.getString("country"),
                                "100",
                                main.getDouble("temp"),
                                condition.getJSONObject(0).getString("description"),
                                main.getDouble("humidity"),
                                main.getDouble("feels_like"),
                                main.getDouble("pressure")
                        )
                        txtHomeCity.text = city
                        txtHomeCountry.text = w.country
                        txtHomeCondition.text = w.condition
                        txtHomeTemp.text = w.temp.toString()+"°C"
                        var coordinates = it.getJSONObject("coord")
                        lat = coordinates.getDouble("lat")
                        lon = coordinates.getDouble("lon")
                    }, Response.ErrorListener {

            }){

            }
            queue.add(jsonObjectRequest)
            list.clear()
            fetchData(lat,lon,count)


        }else{
            val dialog =  AlertDialog.Builder(this)
            dialog.setTitle("Some Problem occurred")
                    .setMessage("No Internet Connection ")
                    .setPositiveButton("Open Settings"){ text, listener ->

                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()

                    }
                    .setNegativeButton("Exit"){ text, listener ->
                        ActivityCompat.finishAffinity(this)
                    }
                    .create().show()
        }
    }

    private fun fetchData(lat: Double, lon: Double, count: Int) {

        list.clear()
        val url1 ="http://api.openweathermap.org/data/2.5/find?lat=$lat&lon=$lon&cnt=$count&appid=e525c6a59c9c327c7720388ee28708d2&units=metric"
        val queue = Volley.newRequestQueue(this)


        if (ConnectionManager().checkConnectivity(this)) {


            val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url1, null,
                    Response.Listener<JSONObject> {

                        Log.e("response", "success is $it")
                        val jsonArray = it.getJSONArray("list")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val main = jsonObject.getJSONObject("main")
                            val condition = jsonObject.getJSONArray("weather")


                            val weatherInfo = WeatherInfo(
                                    jsonObject.getString("name"),
                                    jsonObject.getJSONObject("sys").getString("country"),
                                    jsonObject.getString("dt"),
                                    main.getDouble("temp"),
                                    condition.getJSONObject(0).getString("description"),
                                    main.getDouble("humidity"),
                                    main.getDouble("feels_like"),
                                    main.getDouble("pressure")
                            )
                            list.add(weatherInfo)
                        }
                        homeRecyclerAdapter = WeatherAdapter(this,list)
                        homeRecyclerView.itemAnimator = DefaultItemAnimator()
                        homeRecyclerView.adapter = homeRecyclerAdapter

                    }, Response.ErrorListener {

                Log.e("response", "error is $it")

            }) {

            }
            queue.add(jsonObjectRequest)
        }else{
            val dialog =  AlertDialog.Builder(this)
            dialog.setTitle("Some Problem occurred")
                    .setMessage("No Internet Connection ")
                    .setPositiveButton("Open Settings"){ text, listener ->

                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()

                    }
                    .setNegativeButton("Exit"){ text, listener ->
                        ActivityCompat.finishAffinity(this)
                    }
                    .create().show()
        }


    }
}