package com.example.weather.logic.model

import com.google.gson.annotations.SerializedName

//定义解析json后产生的数据类结构

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String,
    val location: Location,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)