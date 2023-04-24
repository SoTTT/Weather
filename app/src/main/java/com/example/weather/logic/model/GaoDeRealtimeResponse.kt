package com.example.weather.logic.model

import com.google.gson.annotations.SerializedName

data class GaoDeRealtimeResponse(
    val status: String,
    val count: Int,
    val info: String,
    val infocode: String,
    val lives: List<Realtime>
) {
    data class Realtime(
        val province: String,
        val city: String,
        val adcode: String,
        val weather: String,
        val temperature: String,
        @SerializedName("winddirection") val windDirection: String,
        @SerializedName("windpower") val windPower: String,
        val humidity: String,
        @SerializedName("reporttime") val reportTime: String,
        @SerializedName("temperature_float") val temperatureFloat: Float,
        @SerializedName("humidity_float") val humidityFloat: Float
    )
}

