package com.example.weather.logic.network

import com.example.weather.logic.model.GaoDeForecastResponse
import com.example.weather.logic.model.GaoDePlaceResponse
import com.example.weather.logic.model.GaoDeRealtimeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


//WeatherNetWork是网络数据访问的入口
object WeatherNetwork {

    //使用ServiceCreator创建代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    private val weatherService = ServiceCreator.create<WeatherService>()

    private val gaoDeWeatherService = ServiceCreator.create<GaoDeForecastService>()

    private val gaoDePlaceService = ServiceCreator.create<GaoDePlaceService>()


    //await是个对Call<T>的扩展函数，也是个挂起函数
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            this.enqueue(object : Callback<T> {

                //onResponse是响应成功的回调函数
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                //onFailure是响应失败的回调函数
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    suspend fun searchPlaces(query: String) = placeService.searchPlace(query).await()

    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng).await()

    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    suspend fun searchGaoDePlaces(query: String): GaoDePlaceResponse =
        gaoDePlaceService.searchPlace(query).await()

    suspend fun getGaoDeRealtimeWeather(adcode: String): GaoDeRealtimeResponse =
        gaoDeWeatherService.getRealtime(adcode).await()

    suspend fun getDailyWeather(adcode: String): GaoDeForecastResponse =
        gaoDeWeatherService.getForecast(adcode).await()
}