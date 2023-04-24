package com.example.weather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//该类对创建Retrofit对象的行为进行了封装，在其中保存了要用到的服务器的根路径
object ServiceCreator {

    private const val BASE_URL = "https://restapi.amap.com/v3/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}