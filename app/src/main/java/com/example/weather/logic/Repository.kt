package com.example.weather.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.weather.logic.dao.PlaceDao
import com.example.weather.logic.model.PlaceResponse.Place
import com.example.weather.logic.model.Weather
import com.example.weather.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.RuntimeException

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

    fun refreshWeather(lng: String, lat: String): LiveData<Result<Weather>> {
        return liveData(Dispatchers.IO) {
            val result = try {
                coroutineScope {
                    val deferredRealtime = async {
                        WeatherNetwork.getRealtimeWeather(lng, lat)
                    }
                    val deferredDaily = async {
                        WeatherNetwork.getDailyWeather(lng, lat)
                    }
                    val realtimeResponse = deferredRealtime.await()
                    val dailyResponse = deferredDaily.await()
                    if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                        val weather =
                            Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                        Result.success(weather)
                    } else {
                        @Suppress("ThrowableNotThrown")
                        Result.failure(
                            RuntimeException(
                                "realtime response status is ${realtimeResponse.status} "
                                        + "and daily response status is ${dailyResponse.status}"
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Result.failure<Weather>(e);
            }
            emit(result)
        }
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

}