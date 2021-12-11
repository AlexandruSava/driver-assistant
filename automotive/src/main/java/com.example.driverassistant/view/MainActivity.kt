package com.example.driverassistant.view

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.driverassistant.R

class MainActivity : AppCompatActivity() {
    private lateinit var car: Car
    private lateinit var carPropertyManager: CarPropertyManager
    private val permissions = arrayOf(
        Car.PERMISSION_SPEED,
        Car.PERMISSION_EXTERIOR_ENVIRONMENT
    )

    private lateinit var speedTextView: TextView
    private lateinit var outsideTemperatureTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        speedTextView = findViewById(R.id.speedTextView)
        outsideTemperatureTextView = findViewById(R.id.outsideTemperatureTextView)

        initializeCar()
    }

    override fun onResume() {
        super.onResume()

        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 1)
            }
        }
    }

    override fun onPause() {
        if (car.isConnected) {
            car.disconnect()
        }

        super.onPause()
    }

    private fun initializeCar() {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
            return
        }

        if(::car.isInitialized) {
            return
        }

        car = Car.createCar(this)
        carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager

        listenSensorData()
    }

    private fun listenSensorData() {

        carPropertyManager.registerCallback(object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
                val speed : Float = carPropertyValue.value as Float
                speedTextView.text = (speed * 3.6).toInt().toString()
            }

            override fun onErrorEvent(p0: Int, p1: Int) {
                "Error retrieving sensor data".also { speedTextView.text = it }
            }
        }, VehiclePropertyIds.PERF_VEHICLE_SPEED, CarPropertyManager.SENSOR_RATE_NORMAL)

        carPropertyManager.registerCallback(object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
                outsideTemperatureTextView.text = carPropertyValue.value.toString()
            }

            override fun onErrorEvent(p0: Int, p1: Int) {
                "Error retrieving sensor data".also { outsideTemperatureTextView.text = it }
            }
        }, VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE, CarPropertyManager.SENSOR_RATE_ONCHANGE)
    }
}
