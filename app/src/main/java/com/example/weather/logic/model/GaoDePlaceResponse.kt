package com.example.weather.logic.model

import com.google.gson.annotations.SerializedName

data class GaoDePlaceResponse(
    val status: String,
    val count: String,
    val info: String,
    @SerializedName("pois") val places: List<Place>
) {
    data class Place(
        @SerializedName("cityname") val city: String,
        val citycode: String,
        val location: String,
        val adcode: String,
        val address: String
    ) {
        val lng by lazy {
            location.split(",")[0]
        }

        val lat by lazy {
            location.split(",")[1]
        }
    }

}