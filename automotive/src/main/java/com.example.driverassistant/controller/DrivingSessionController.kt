package com.example.driverassistant.controller

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

    fun initializeDrivingSession() {
        drivingSessionScore = 100f
        maxDrivingSessionScore = 100f
    }

    fun stopDrivingSession() {
        clearSensorDataList()

        println(sensorDataList)
    }

    fun addSensorData(sensorData: SensorData){
        sensorDataList.add(sensorData)
        currentSensorData = sensorData

        println(sensorDataList.size)
        println(sensorDataList.toString())
    }

    private fun clearSensorDataList() {
        sensorDataList.clear()
    }

    fun analyzeDrivingSession() : Float {
        val mistakeRatio = if (currentSensorData.outsideTemp < 3) {
            2f
        } else {
            1f
        }

        val speedRatio = (currentSensorData.speed * 3.6 / (currentSensorData.speedLimit + 10f).toDouble()).toFloat()
        println(speedRatio)

        if (speedRatio > 1) {
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
        } else {
            if (drivingSessionScore + basicScoreGain < maxDrivingSessionScore) {
                drivingSessionScore += basicScoreGain
            } else {
                drivingSessionScore = maxDrivingSessionScore
            }
        }

        println(drivingSessionScore)
        println(maxDrivingSessionScore)

        return drivingSessionScore
    }
}