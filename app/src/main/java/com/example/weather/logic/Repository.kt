package com.example.weather.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.weather.logic.model.Place
import com.example.weather.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {
    fun searchPlaces(query: String): LiveData<Result<List<Place>>> {
        return liveData(Dispatchers.IO) {
            val result = try {
                val placeResponse = WeatherNetwork.searchPlaces(query)
                if (placeResponse.status == "ok") {
                    val places = placeResponse.places
                    Result.success(places)
                } else {
                    Result.failure(RuntimeException("response status is ${placeResponse.status}"))
                }

            } catch (e: Exception) {
                Result.failure<List<Place>>(e)
            }
            emit(result)
        }
    }
}