package com.example.weather.logic.network

import com.example.weather.WeatherApplication
import com.example.weather.logic.model.DailyResponse
import com.example.weather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("weather/weatherInfo?key=${WeatherApplication.KEY}?city={adcode}?extensions=base")
    fun getRealtimeWeather(
        @Path("adcode") code: String,
    ): Call<RealtimeResponse>

    @GET("v2.5/${WeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>

}