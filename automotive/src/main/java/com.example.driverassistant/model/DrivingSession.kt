package com.example.driverassistant.model

data class DrivingSession(
    val userId: String,
    val email: String,
    val startTime: Long,
    val endTime: Long,
    val finalScore: Float,
    val finalMaximumScore: Float,
    val notificationsList: ArrayList<Notification>,
)
