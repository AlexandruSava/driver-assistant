package com.example.driverassistant.controller

import com.example.driverassistant.model.SensorData
import kotlin.random.Random

class MainController {
    private var sensorDataList: ArrayList<SensorData> = ArrayList()
    private lateinit var drivingSessionScore: Number

    fun initializeDrivingSession() {
        drivingSessionScore = 100
    }

    fun stopDrivingSession() {
        clearSensorDataList()

        println(sensorDataList)
    }

    fun addSensorData(sensorData: SensorData){
        sensorDataList.add(sensorData)

        println(sensorDataList.size)
        println(sensorDataList.toString())
    }

    private fun clearSensorDataList() {
        sensorDataList.clear()

        println(sensorDataList)
    }

    fun analyzeDrivingSession() : Int {
        val nextInt = Random.nextInt(0, 100)
        println(nextInt)
        return nextInt
    }
}