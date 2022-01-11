package com.example.driverassistant.controller

import com.example.driverassistant.model.DrivingSession

class DashboardController {

    fun calculateUserScore(drivingSessionList: ArrayList<DrivingSession>): Int {
        var sum = 0f
        for (drivingSession in drivingSessionList) {
            sum += drivingSession.finalScore
        }
        return (sum / drivingSessionList.size.toFloat()).toInt()
    }
}