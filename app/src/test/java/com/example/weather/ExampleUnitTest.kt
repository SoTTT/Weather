package com.example.weather

import android.util.Log
import com.example.weather.logic.network.GaoDePlaceService
import com.example.weather.logic.network.ServiceCreator
import junit.framework.TestCase
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest : TestCase() {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}