package com.example.driverassistant.controller

import android.util.Log
import com.example.driverassistant.model.SensorData
import kotlin.math.pow

class DrivingSessionController {

    private var sensorDataList: ArrayList<SensorData> = ArrayList()
    private lateinit var currentSensorData: SensorData

    private var drivingSessionScore: Float = 100f
    private var maxDrivingSessionScore: Float = 100f

    private val basicScoreReduction: Float = 0.3f
    private val basicScoreGain: Float = 0.3f

    private val basicPower: Float = 2f

    fun startDrivingSession() {
        drivingSessionScore = 100f
        maxDrivingSessionScore = 100f
    }

    fun stopDrivingSession() {
        clearSensorDataList()
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
            reduceDrivingScore(mistakeRatio, speedRatio)
        } else {
            increaseDrivingScore()
        }

        Log.d("SensorData:", "index: ${sensorDataList.size}, speedRatio: $speedRatio, " +
                "drivingSessionScore: $drivingSessionScore, maxDrivingSessionScore: " +
                "$maxDrivingSessionScore")

        return drivingSessionScore
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
        println(mistakeScoreReduction)

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
}