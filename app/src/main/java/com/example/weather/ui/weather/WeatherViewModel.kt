package com.example.weather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weather.logic.Repository
import com.example.weather.logic.model.PlaceResponse.Location

class WeatherViewModel : ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    private val adcodeLiveData = MutableLiveData<String>()

    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    var adcode = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    val gaoDeWeatherLiveData = Transformations.switchMap(adcodeLiveData) {
        Repository.refreshWeather(it)
    }


    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }

    fun refreshWeather(adcode: String) {
        adcodeLiveData.value = adcode
    }

}