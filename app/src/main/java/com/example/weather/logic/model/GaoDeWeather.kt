package com.example.weather.logic.model

data class GaoDeWeather(
    val realtimeWeather: GaoDeRealtimeResponse.Realtime,
    val forecastWeather: List<GaoDeForecastResponse.Forecast>
)