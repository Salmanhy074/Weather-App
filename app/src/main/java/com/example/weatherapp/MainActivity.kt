package com.example.weatherapp

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        fetchWeatherData("Hafizabad")
        searchCity()

    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    private fun fetchWeatherData(cityName : String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityName, "5ccbefcb890f751e58f38c8f4cf62bbe", "metric")
        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val temperature = responseBody.main.temp
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    //val maxTemp = responseBody.main.temp_max
                    val realFeel = responseBody.main.feels_like

                    //Log.d("WeatherData", "Current Temp: $temperature, Max Temp: $maxTemp, Min Temp: $minTemp")

                    //Toast.makeText(this@MainActivity, "Current Temp: $temperature, Max Temp: $maxTemp, Min Temp: $minTemp", Toast.LENGTH_SHORT).show()

                    //Log.d("TAG", "onResponse: $temperature")
                    //Toast.makeText(this@MainActivity, "$temperature", Toast.LENGTH_SHORT).show()

                    binding.temprature.text = "$temperature °C"
                    binding.humidity.text = "$humidity %"
                    binding.wind.text = "$windSpeed m/s"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text = "${time(sunset)}"
                    binding.sea.text = "$seaLevel hPa"
                    binding.weatherCondition.text = "$condition"
                    //binding.maxTemp.text = "$maxTemp °C"
                    //binding.minTemp.text = "$minTemp °C"
                    binding.minTemp.text = "$realFeel °C"
                    binding.rain.text = "$condition"
                    binding.weatherCondition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                        binding.date.text = date()
                        binding.cityName.text = "$cityName"



                    //binding.cityName.text = "$cityName"

                    changeBackgroundImagesAccordingToWeatherCondition(condition)



                }
            }



             private fun changeBackgroundImagesAccordingToWeatherCondition(conditions: String) {
                 when(conditions){
                     /*"Haze" ->{
                         binding.root.setBackgroundResource(R.drawable.colud_background)
                         binding.lottieAnimationView.setAnimation(R.raw.cloud)
                     }*/

                     "Clear Sky", "Sunny", "Clear", "Fair" ->{
                         binding.root.setBackgroundResource(R.drawable.sunny_background)
                         binding.lottieAnimationView.setAnimation(R.raw.sun)
                         binding.conditions.setBackgroundResource(R.drawable.sun)
                     }

                     "Partly Clouds", "Clouds", "Overcast", "Mist", "Fog", "Smoke", "Haze", "Dust", "Sand", "Ash", "Squall", "Tornado" ->{
                         binding.root.setBackgroundResource(R.drawable.colud_background)
                         binding.lottieAnimationView.setAnimation(R.raw.cloud)
                         binding.conditions.setBackgroundResource(R.drawable.clouds)
                     }

                     "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain", "Thunderstorm", "Thunderstorm with Light Rain", "Thunderstorm with Rain", "Thunderstorm with Heavy Rain", "Thunderstorm with Thunderstorm", "Rain" ->{
                         binding.root.setBackgroundResource(R.drawable.rain_background)
                         binding.lottieAnimationView.setAnimation(R.raw.rain)
                         binding.conditions.setBackgroundResource(R.drawable.rainny)
                     }

                     "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard", "Snow", "Snow Showers", "Heavy Snow Showers" ->{
                         binding.root.setBackgroundResource(R.drawable.snow_background)
                         binding.lottieAnimationView.setAnimation(R.raw.snow)
                         binding.conditions.setBackgroundResource(R.drawable.mist)
                     }

                    /* else ->{
                             binding.root.setBackgroundResource(R.drawable.sunny_background)
                             binding.lottieAnimationView.setAnimation(R.raw.sun)
                     }*/

                 }
                 binding.lottieAnimationView.playAnimation()
             }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to get weather data", Toast.LENGTH_SHORT).show()
            }

        } )
    }

    fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((java.util.Date()))
    }

    /*fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((java.util.Date(timestamp*1000)))
    }*/

    fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }


    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((java.util.Date()))

    }
}