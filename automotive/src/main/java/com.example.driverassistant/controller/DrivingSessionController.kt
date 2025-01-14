package com.example.driverassistant.controller

import android.util.Log
import com.example.driverassistant.model.DrivingSession
import com.example.driverassistant.model.Notification
import com.example.driverassistant.model.SensorData
import com.example.driverassistant.model.WarningEvent
import kotlin.math.pow

class DrivingSessionController {

    private lateinit var drivingSession: DrivingSession

    private lateinit var userId: String
    private lateinit var email: String
    private var startTime: Long = 0
    private var drivingSessionScore: Float = 100f
    private var maxDrivingSessionScore: Float = 100f
    private var warningEventsList = ArrayList<WarningEvent>()

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
            warningEventsList
        )
        clearSensorDataList()
    }

    private fun getAverageSpeed(): Float {
        var sum = 0f
        for (sensorData in sensorDataList) {
            sum += sensorData.speed
        }
        return sum / sensorDataList.size.toFloat()
    }

    fun addSensorData(sensorData: SensorData) {
        sensorDataList.add(sensorData)
        currentSensorData = sensorData
    }

    private fun clearSensorDataList() {
        sensorDataList.clear()
    }

    fun analyzeDrivingSession(): Float {
        val mistakeRatio = if (currentSensorData.outsideTemp < 3) 2f else 1f

        val speedRatio =
            (currentSensorData.speed / (currentSensorData.speedLimit + 10f).toDouble()).toFloat()

        if (speedRatio > 1) {
            speedingTimes++
            reduceDrivingScore(mistakeRatio, speedRatio)
            issueSpeedWarningEvent()
        } else {
            increaseDrivingScore()
        }

        Log.d(
            "SensorData:", "index: ${sensorDataList.size}, speedRatio: $speedRatio, " +
                    "drivingSessionScore: $drivingSessionScore, maxDrivingSessionScore: " +
                    "$maxDrivingSessionScore"
        )

        return drivingSessionScore
    }

    private fun issueSpeedWarningEvent() {
        val warningEvent = WarningEvent(
            Notification.SPEEDING.name.lowercase(),
            System.currentTimeMillis(),
            currentSensorData
        )
        warningEventsList.add(warningEvent)
        println(warningEvent)
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

    fun getLastWarningEvent(): WarningEvent {
        return if (speedingTimes > 0) {
            warningEventsList.last()
        } else {
            WarningEvent(
                Notification.GOOD_DRIVING.name.lowercase(),
                System.currentTimeMillis(),
                currentSensorData
            )
        }
    }

    fun getDrivingSession(): DrivingSession {
        return drivingSession
    }
}