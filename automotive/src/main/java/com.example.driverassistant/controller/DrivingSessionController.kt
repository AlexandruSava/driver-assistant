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
    private var drivingSessionScore: Float = 100f
    private var maxDrivingSessionScore: Float = 100f
    private var notificationList = ArrayList<Notification>()

    private var speedingTimes: Int = 0

    private var sensorDataList = ArrayList<SensorData>()
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
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val averageSpeed = getAverageSpeed()
        drivingSession = DrivingSession(
            1,
            userId,
            email,
            startTime,
            endTime,
            duration,
            averageSpeed,
            drivingSessionScore,
            maxDrivingSessionScore,
            notificationList
        )
        clearSensorDataList()
    }

    private fun getAverageSpeed(): Float {
        var sum = 0f
        for (sensorData in sensorDataList) {
            sum += sensorData.speed
        }
        return (sum / sensorDataList.size.toFloat()).toFloat()
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
            "We just noticed that you were speeding $speedingTimes times in this session.",
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

    fun getLastNotification(): Notification {
        return if (speedingTimes > 0) {
            notificationList.last()
        } else {
            Notification(
                "good-driving",
                "Drive carefully",
                "We noticed you're quite a good driver. No mistakes yes.",
                0,
                0L
            )
        }
    }

    fun getDrivingSession(): DrivingSession {
        return drivingSession
    }
}