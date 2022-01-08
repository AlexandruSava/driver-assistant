package com.example.driverassistant.model

data class Notification(
    val type: String,
    val title: String,
    val message: String,
    val timesIssued: Number,
    val timestamp: Long
)