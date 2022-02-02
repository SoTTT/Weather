package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        viewMainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewMainBinding.root)
    }
}

