package com.example.weather.logic.network

import com.example.weather.WeatherApplication
import com.example.weather.logic.model.GaoDePlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GaoDePlaceService {

    @GET("geocode/geo?key=${WeatherApplication.KEY}")
    fun searchPlace(@Query("address") keywords: String): Call<GaoDePlaceResponse>

}