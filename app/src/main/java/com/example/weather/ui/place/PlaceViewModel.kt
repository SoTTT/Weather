package com.example.weather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weather.logic.Repository
import com.example.weather.logic.model.Place

class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    //要给placeLiveData的LiveData是从外部返回的，所以用switchMap方法转换
    val placeLiveData = Transformations.switchMap(searchLiveData) {
        Repository.searchPlaces(it)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

}