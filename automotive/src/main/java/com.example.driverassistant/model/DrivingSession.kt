package com.example.driverassistant.model

import java.io.Serializable

data class DrivingSession(
    var index: Int,
    val userId: String,
    val email: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Long,
    val averageSpeed: Float,
    val finalScore: Float,
    val finalMaximumScore: Float,
    val notificationsList: ArrayList<Notification>,
) : Serializable
