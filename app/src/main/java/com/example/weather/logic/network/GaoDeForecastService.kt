package com.example.weather.logic.network


import com.example.weather.WeatherApplication
import com.example.weather.logic.model.GaoDeForecastResponse
import com.example.weather.logic.model.GaoDeRealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GaoDeForecastService {

    @GET("weather/weatherInfo?key=${WeatherApplication.KEY}&extensions=all")
    fun getForecast(@Query("city") keywords: String): Call<GaoDeForecastResponse>

    @GET("weather/weatherInfo?key=${WeatherApplication.KEY}&extensions=base")
    fun getRealtime(@Query("city") keywords: String): Call<GaoDeRealtimeResponse>

}