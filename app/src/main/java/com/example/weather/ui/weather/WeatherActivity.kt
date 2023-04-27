package com.example.weather.ui.weather

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.ActivityWeatherBinding
import com.example.weather.logic.model.GaoDeWeather
import com.example.weather.logic.model.Weather
import com.example.weather.logic.model.fromContextStringSource
import com.example.weather.logic.model.getSky
import com.example.weather.util.Util
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    val viewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //处理沉浸式状态栏的方法，但在API30中已被弃用
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT


        setContentView(binding.root)
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.adcode.isEmpty()) {
            viewModel.adcode = intent.getStringExtra("place_adcode") ?: ""
        }
        viewModel.gaoDeWeatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if (weather != null) {
                showGaoDeWeatherInfo(weather)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.hint_of_get_sky_information_error),
                    Toast.LENGTH_LONG
                ).show()
                it.exceptionOrNull()?.printStackTrace()
                binding.swipeRefresh.isRefreshing = false
            }
        })
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        binding.now.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        refreshWeather()
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.adcode)
        binding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.now.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        //处理now中的数据和样式
        val currentTempText = "${realtime.temperature.toInt()}℃";
        binding.now.currentTemp.text = currentTempText;

        binding.now.currentSky.text = getSky(realtime.skycon).info.fromContextStringSource(this)

        val currentPM25Text =
            "${getString(R.string.air_index)} ${realtime.airQuality.aqi.chn.toInt()}"
        binding.now.currentAQI.text = currentPM25Text
        binding.now.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        binding.forecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this)
                .inflate(R.layout.forecast_item, binding.forecast.forecastLayout, false)
            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info.fromContextStringSource(this)
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            binding.forecast.forecastLayout.addView(view)

        }

        //处理file index部分的数据和布局
        val lifeIndex = daily.lifeIndex
        binding.fileIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.fileIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.fileIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.fileIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE

    }

    private fun showGaoDeWeatherInfo(weather: GaoDeWeather) {
        binding.now.placeName.text = viewModel.placeName
        val realtime = weather.realtimeWeather
        val forecasts = weather.forecastWeather

        binding.now.currentTemp.text = realtime.temperature.toFloat().toString()
        binding.now.currentSky.text =
            Util.castGaoDeSkyConToSkyCon(realtime.weather).info.fromContextStringSource(this)

        binding.now.currentAQI.text = "unknown"
        binding.now.nowLayout.setBackgroundResource(Util.castGaoDeSkyConToSkyCon(realtime.weather).bg)

        binding.forecast.forecastLayout.removeAllViews()

        val days = forecasts[0].casts.size
        for (index in 0 until days) {
            val forecast = forecasts[0].casts[index]
            val dayTemperature = forecast.dayTemperature
            val nightTemperature = forecast.nightTemperature
            val view = LayoutInflater.from(this)
                .inflate(R.layout.forecast_item, binding.forecast.forecastLayout, false)

            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)

            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateInfo.text = forecast.date.split(" ")[0]
            val sky = Util.castGaoDeSkyConToSkyCon(forecast.dayWeather)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info.fromContextStringSource(this)

            val tempText = "${nightTemperature.toFloat()} ~ ${dayTemperature.toFloat()}"
            temperatureInfo.text = tempText

            binding.forecast.forecastLayout.addView(view)


        }

        val unknownText = "unknown"
        binding.fileIndex.coldRiskText.text = unknownText
        binding.fileIndex.carWashingText.text = unknownText
        binding.fileIndex.ultravioletText.text = unknownText
        binding.fileIndex.dressingText.text = unknownText
        binding.weatherLayout.visibility = View.VISIBLE

        binding.swipeRefresh.isRefreshing = false
    }
}