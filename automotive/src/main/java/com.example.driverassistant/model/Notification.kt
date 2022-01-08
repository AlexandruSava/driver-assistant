package com.example.driverassistant.model

import java.sql.Timestamp

data class Notification(
    val type: String,
    val title: String,
    val message: String,
    val timesIssued: Number,
    val timestamp: Timestamp
)