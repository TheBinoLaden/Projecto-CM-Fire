package com.example.finalproject.weather

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Connect {
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        fun callApi(): CallAPI {
            return Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl(BASE_URL)
                .build()
                .create(CallAPI::class.java)
        }
    }
}