package com.example.weather

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.logic.network.GaoDeForecastService
import com.example.weather.logic.network.GaoDePlaceService
import com.example.weather.logic.network.ServiceCreator
import kotlinx.coroutines.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.weather", appContext.packageName)
    }

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            this.enqueue(object : Callback<T> {

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    @Test
    fun useGaoDePlaceAPI() {
        val service = ServiceCreator.create<GaoDePlaceService>()
        val castService = ServiceCreator.create<GaoDeForecastService>()
        runBlocking {
            val response = async {
                service.searchPlace("山西大学").await()
            }
//            val code = response.await().places[0].adcode
            Log.d("TEST", "useGaoDePlaceAPI: ${response.await()}")
//            val castResponse = async {
//                castService.getForecast(code).await()
//            }
//            Log.d("TEST", "useGaoDePlaceAPI: ${castResponse.await()}")
//            val realtimeResponse = async {
//                castService.getRealtime(code).await()
//            }
//            Log.d("TEST", "useGaoDePlaceAPI: ${realtimeResponse.await()}")
        }
        assert(true)
    }
}