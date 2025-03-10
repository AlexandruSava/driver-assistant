package com.example.driverassistant.controller

import com.example.driverassistant.model.DrivingSession

class DashboardController {

    fun calculateUserScore(drivingSessionList: ArrayList<DrivingSession>): Int {
        var totalDuration = 0L
        for (drivingSession in drivingSessionList) {
            totalDuration += drivingSession.duration
        }

        var ponderedAverage = 0f
        for (drivingSession in drivingSessionList) {
            val weight = (drivingSession.duration / totalDuration.toFloat())
            ponderedAverage += weight * drivingSession.finalScore
        }
        return ponderedAverage.toInt()
    }
}