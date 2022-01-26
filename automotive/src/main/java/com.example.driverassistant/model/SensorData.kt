package com.example.driverassistant.model

data class SensorData(
    val speed: Int,
    val outsideTemp: Int,
    val latitude: Double,
    val longitude: Double,
    val speedLimit: Int
)