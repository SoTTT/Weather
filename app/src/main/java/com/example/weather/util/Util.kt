package com.example.weather.util


import com.example.weather.logic.model.Sky
import com.example.weather.logic.model.getSky

object Util {

    private val skyCon = mapOf(
//        "白天晴" to "CLEAR_DAY",
        "晴" to "CLEAR_NIGHT",
        "阴" to "PARTLY_CLOUDY_NIGHT",
//        "白天少云" to "PARTLY_CLOUDY",
        "少云" to "PARTLY_CLOUDY_NIGHT",
        "多云" to "CLOUDY",
        "有风" to "WIND",
        "小雨" to "LIGHT_RAIN",
        "中雨" to "MODERATE_RAIN",
        "大雨" to "HEAVY_RAIN",
        "暴雨" to "STORM_RAIN",
        "雷阵雨" to "THUNDER_SHOWER",
        "雨夹雪" to "SLEET",
        "小雪" to "LIGHT_SNOW",
        "中雪" to "MODERATE_SNOW",
        "大雪" to "HEAVY_SNOW",
        "暴雪" to "STORM_SNOW",
        "冰雹" to "HAIL",
        "霾" to "LIGHT_HAZE",
        "中度霾" to "MODERATE_HAZE",
        "重度霾" to "HEAVY_HAZE",
        "雾" to "FOG",
        "浮尘" to "DUST"
    )

    fun castGaoDeSkyConToSkyCon(skyCon: String): Sky {
        val sky = this.skyCon[skyCon]
            ?: throw RuntimeException("can`t find skyCon in map of GaoDe skyCon to skyCon : GaoDeSkyCon is $skyCon")
        return getSky(sky)
    }
}