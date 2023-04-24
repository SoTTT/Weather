package com.example.weather.logic.network

import com.example.weather.WeatherApplication
import com.example.weather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {


    //searchPlace返回的应该是PlaceService类的对象
    //Retrofit库规定接口函数的返回值应该使用Call接口并将真正要返回的类型包裹起来
    //searchPlace函数用来发起搜索城市的数据请求
    @GET("place/text?key=${WeatherApplication.KEY}&extensions=all")
    fun searchPlace(@Query("keywords") keywords: String): Call<PlaceResponse>

}