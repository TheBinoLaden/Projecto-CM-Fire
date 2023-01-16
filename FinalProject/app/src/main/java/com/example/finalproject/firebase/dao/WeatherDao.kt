package com.example.finalproject.firebase.dao

import com.example.finalproject.weather.District
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.Date

class WeatherDao {
    companion object {
        fun createDistricts() {
            val dbConnection = Firebase.firestore
            val dataSet = hashMapOf(
                "temp" to 0f,
                "fireRisk" to "None",
                "windSpeed" to 0f,
                "windDeg" to 0,
                "humidity" to 0f,
                "lastUpdate" to 0
            )
            for (district in District.Districts) {
                dbConnection.collection("district").document(district.name).set(dataSet)
            }
        }

        fun getDistrictWeather(district: District.District, callback: (dataSet: HashMap<String, Any>) -> Unit) {
            Firebase.firestore.collection("district").document(district.name).get().addOnCompleteListener { result ->
                val temp = result.result.get("temp") as Double
                val fireRisk = result.result.get("fireRisk") as String
                val windSpeed = result.result.get("windSpeed") as Double
                val windDeg = result.result.get("windDeg") as Long
                val humidity = result.result.get("humidity") as Double
                val lastUpdate = result.result.get("lastUpdate") as Long
                callback(hashMapOf(
                    "temp" to temp,
                    "fireRisk" to fireRisk,
                    "windSpeed" to windSpeed,
                    "windDeg" to windDeg,
                    "humidity" to humidity,
                    "lastUpdate" to lastUpdate
                ))
            }
        }

        fun setDistrictWeather(district: District.District, dataSet: HashMap<String, Any>) {
            val dbConnection = Firebase.firestore
            dbConnection.collection("district").document(district.name).set(dataSet)
        }
    }
}