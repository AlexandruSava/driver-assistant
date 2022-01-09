package com.example.driverassistant.controller

import android.util.Log
import com.example.driverassistant.model.DrivingSession
import com.example.driverassistant.model.Notification
import com.example.driverassistant.model.SensorData
import kotlin.math.pow

class DrivingSessionController {

    private lateinit var drivingSession: DrivingSession

    private lateinit var userId: String
    private lateinit var email: String
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var drivingSessionScore: Float = 100f
    private var maxDrivingSessionScore: Float = 100f
    private var notificationList: ArrayList<Notification> = ArrayList()

    private var speedingTimes: Int = 0

    private var sensorDataList: ArrayList<SensorData> = ArrayList()
    private lateinit var currentSensorData: SensorData

    private val basicScoreReduction: Float = 0.3f
    private val basicScoreGain: Float = 0.3f

    private val basicPower: Float = 2f

    fun startDrivingSession(userId: String, email: String) {
        this.userId = userId
        this.email = email
        startTime = System.currentTimeMillis()
        drivingSessionScore = 100f
        maxDrivingSessionScore = 100f
    }

    fun stopDrivingSession() {
        endTime = System.currentTimeMillis()
        drivingSession = DrivingSession(
            userId,
            email,
            startTime,
            endTime,
            drivingSessionScore,
            maxDrivingSessionScore,
            notificationList
        )
        println(drivingSession)
        clearSensorDataList()
        clearNotificationDataList()
    }

    private fun clearNotificationDataList() {
        notificationList.clear()
    }

    fun addSensorData(sensorData: SensorData){
        sensorDataList.add(sensorData)
        currentSensorData = sensorData
    }

    private fun clearSensorDataList() {
        sensorDataList.clear()
    }

    fun analyzeDrivingSession() : Float {
        val mistakeRatio = if (currentSensorData.outsideTemp < 3) 2f else 1f

        val speedRatio = (currentSensorData.speed * 3.6 / (currentSensorData.speedLimit + 10f).toDouble()).toFloat()

        if (speedRatio > 1) {
            speedingTimes++
            reduceDrivingScore(mistakeRatio, speedRatio)
            issueSpeedNotification(speedingTimes)
        } else {
            increaseDrivingScore()
        }

        Log.d("SensorData:", "index: ${sensorDataList.size}, speedRatio: $speedRatio, " +
                "drivingSessionScore: $drivingSessionScore, maxDrivingSessionScore: " +
                "$maxDrivingSessionScore")

        return drivingSessionScore
    }

    private fun issueSpeedNotification(speedingTimes: Int) {
        val notification = Notification(
            "speeding",
            "Respect Speed Limit",
            "We just noticed that you were speeding $speedingTimes in this session.",
            speedingTimes,
            System.currentTimeMillis()
        )
        notificationList.add(notification)
        println(notification)
    }

    private fun increaseDrivingScore() {
        if (drivingSessionScore + basicScoreGain < maxDrivingSessionScore) {
            drivingSessionScore += basicScoreGain
        } else {
            drivingSessionScore = maxDrivingSessionScore
        }
    }

    private fun reduceDrivingScore(mistakeRatio: Float, speedRatio: Float) {
        val mistakeScoreReduction = basicPower.pow(basicScoreReduction * mistakeRatio * speedRatio)

        if (mistakeScoreReduction * 0.3f > maxDrivingSessionScore) {
            maxDrivingSessionScore = 0f
        } else {
            maxDrivingSessionScore -= mistakeScoreReduction * 0.3f
        }

        if (mistakeScoreReduction > drivingSessionScore) {
            drivingSessionScore = 0f
        } else {
            drivingSessionScore -= mistakeScoreReduction
        }
    }

    fun getNotificationList(): ArrayList<Notification> {
        return notificationList
    }
}