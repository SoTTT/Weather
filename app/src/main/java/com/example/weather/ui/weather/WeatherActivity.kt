package com.example.weather.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {

    private val activityWeatherBinding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityWeatherBinding.root)
    }
}