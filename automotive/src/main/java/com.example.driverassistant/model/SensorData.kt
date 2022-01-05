package com.example.driverassistant.model

import android.location.Location

data class SensorData(
    val speed: Int,
    val outsideTemp: Int,
    val location: Location,
    val speedLimit: Int
)