package com.example.finalproject.weather

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CallAPI {
    @GET("weather")
    fun getWeatherInfo(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") apiKey: String
    ): Observable<Model.Result>
}