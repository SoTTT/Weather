package com.example.weather

<<<<<<< HEAD
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeatherApplication : Application() {


    //Application类应该保存着整个应用应该使用的通用数据
    //包含请求数据要用到的Token值和一个在全局范围内生效的context
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val TOKEN = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext;
    }

=======
class WeatherApplication {
>>>>>>> ff671ce83b4990ca6c80f5433a02cd096b28bada
}