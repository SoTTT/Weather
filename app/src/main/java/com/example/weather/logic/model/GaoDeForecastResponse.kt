package com.example.weather.logic.model

import com.google.gson.annotations.SerializedName

data class GaoDeForecastResponse(
    val status: String,
    val count: Int,
    val info: String,
    val infocode: String,
    val forecasts: List<Forecast>
) {
    data class Forecast(
        val city: String,
        val adcode: String,
        val province: String,
        @SerializedName("reporttime") val time: String,
        val casts: List<Cast>
    ) {
        data class Cast(
            val date: String,
            val week: Int,
            @SerializedName("dayweather") val dayWeather: String,
            @SerializedName("nightweather") val nightWeather: String,
            @SerializedName("daytemp") val dayTemperature: String,
            @SerializedName("nighttemp") val nightTemperature: String,
            @SerializedName("nightwind") val nightWind: String,
            @SerializedName("daywind") val dayWind: String,
            @SerializedName("daypower") val dayPower: String,
            @SerializedName("nightpower") val nightPower: String,
        )

    }
}

