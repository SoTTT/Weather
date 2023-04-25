package com.example.weather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.weather.WeatherApplication
import com.example.weather.logic.model.GaoDePlaceResponse
import com.example.weather.logic.model.PlaceResponse.Place
import com.google.gson.Gson

object PlaceDao {

    private fun sharedPreferences() = WeatherApplication.context.getSharedPreferences(
        "weather",
        Context.MODE_PRIVATE
    )

    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun savePlace(place: GaoDePlaceResponse.Place) {
        sharedPreferences().edit {
            putString("gao_de_place", Gson().toJson(place))
        }
    }

    fun isGaoDePlaceSaved() = sharedPreferences().contains("gao_de_place")

    fun getSavedGaoDePlace(): GaoDePlaceResponse.Place = Gson().fromJson(
        sharedPreferences().getString("gao_de_place", ""),
        GaoDePlaceResponse.Place::class.java
    )


}