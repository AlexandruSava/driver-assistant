package com.example.driverassistant.model

data class WarningEvent(
    val type: String,
    val timestamp: Long,
    val sensorData: SensorData
)