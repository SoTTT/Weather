package com.example.weather.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.weather.logic.dao.PlaceDao
import com.example.weather.logic.model.GaoDePlaceResponse
import com.example.weather.logic.model.GaoDeWeather
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
                if (placeResponse.status.lowercase() == "ok") {
                    val places = placeResponse.places
                    Result.success(places)
                } else {
                    Result.failure(RuntimeException("response status is ${placeResponse.status}"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }

    fun searchGaoDePlaces(query: String): LiveData<Result<List<GaoDePlaceResponse.Place>>> {
        return liveData(Dispatchers.IO) {
            val result = try {
                val response = WeatherNetwork.searchGaoDePlaces(query)
                if (response.status.lowercase() == "ok") {
                    Result.success(response.places)
                } else {
                    Result.failure(RuntimeException("response status is ${response.status}"))
                }
            } catch (ex: java.lang.Exception) {
                Result.failure(ex)
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
                Result.failure(e);
            }
            emit(result)
        }
    }

    fun refreshWeather(adcode: String): LiveData<Result<GaoDeWeather>> {
        return liveData(Dispatchers.IO) {
            val result = try {
                coroutineScope {
                    val deferredRealtime = async {
                        WeatherNetwork.getGaoDeRealtimeWeather(adcode)
                    }
                    val deferredForecast = async {
                        WeatherNetwork.getDailyWeather(adcode)
                    }
                    val realtimeResponse = deferredRealtime.await()
                    val forecastResponse = deferredForecast.await()
                    if (realtimeResponse.status.lowercase() == "ok" && forecastResponse.status.lowercase() == "ok") {
                        Result.success(
                            GaoDeWeather(
                                realtimeResponse.lives[realtimeResponse.count],
                                forecastResponse.forecasts
                            )
                        )
                    } else {
                        Result.failure(
                            RuntimeException(
                                "realtime response status is ${realtimeResponse.status} "
                                        + "and daily response status is ${forecastResponse.status}"
                            )
                        )
                    }
                }
            } catch (ex: java.lang.Exception) {
                Result.failure(ex)
            }
            emit(result)
        }
    }


    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    fun savePlace(place: GaoDePlaceResponse.Place) = PlaceDao.savePlace(place)

    fun getSavedGaoDePlace() = PlaceDao.getSavedGaoDePlace()

    fun isGaoDePlaceSaved() = PlaceDao.isPlaceSaved()
}