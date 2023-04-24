package com.example.weather.logic.model

import com.google.gson.annotations.SerializedName

//定义解析json后产生的数据类结构

data class PlaceResponse(val status: String, @SerializedName("pois") val places: List<Place>) {
    data class Place(
        val name: String,
        val location: Location,
        val address: String
    )

    data class Location(val location: String, val adcode: String) {
        val lng by lazy {
            location.split(',')[0];
        }
        val lat by lazy {
            location.split(',')[1];
        }
    }

}


