package com.example.finalproject.utils

import android.util.Log
import com.example.finalproject.firebase.dao.WeatherDao
import com.example.finalproject.weather.APIData
import com.example.finalproject.weather.District
import com.example.finalproject.weather.Formulas
import com.example.finalproject.weather.Model

class WeatherUtils {
    companion object {
        fun getDistrictWeather(district: District.District, callback: (dataSet: HashMap<String, Any>) -> Unit) {
            WeatherDao.getDistrictWeather(district, callback)
        }

        fun setDistrictWeather(district: District.District, dataSet: HashMap<String, Any>) {
            WeatherDao.setDistrictWeather(district, dataSet)
        }

        fun checkIfShouldUpdate(district: District.District, lat: Double, lon: Double, lastUpdate: Long) {
            if (System.currentTimeMillis() - lastUpdate >= 3600000) {
                getWeatherInfo(lat.toFloat(), lon.toFloat()) { dataSet ->
                    setDistrictWeather(district, dataSet)
                }
            }
        }

        fun getWeatherInfo(lat: Float, lon: Float, callback: (dataSet: HashMap<String, Any>) -> Unit) {
            val units = "metric" // Units available: https://openweathermap.org/current#data
            val lang = "pt" // Languages available by prefix: https://openweathermap.org/current#multi
            APIData.getData(lat, lon, units, lang, object : APIData.Response {
                override fun onResponse(data: Model.Result) {
                    val desc: String = data.weather[0].description
                    val temp: Float = data.main.temp
                    val humidity: Float = data.main.humidity
//                val rainVolume: Float = data.rain[0].oneHour
                    val country: String = data.sys.country
                    val windSpeed: Float = data.wind.speed
                    val windDeg: Int = data.wind.deg
                    Log.d("APICALL", data.toString())
                    val fdi = Formulas.fireDangerIndex(temp, humidity, 1f, windSpeed)
                    callback(hashMapOf(
                            "temp" to data.main.temp,
                            "fireRisk" to Formulas.fdiInterpretation(fdi),
                            "windSpeed" to data.wind.speed,
                            "windDeg" to data.wind.deg,
                            "humidity" to data.main.humidity,
                            "lastUpdate" to System.currentTimeMillis()
                    ))
                }

                override fun onFailure(error: Throwable) {
                    Log.e("APICALL", "Could not make call to weather API", error)
                    callback(hashMapOf(
                            "temp" to 0f,
                            "fireRisk" to "Error",
                            "windSpeed" to 0f,
                            "windDeg" to 0,
                            "humidity" to 0f,
                            "lastUpdate" to 0
                    ))
                }
            })
        }
    }
}