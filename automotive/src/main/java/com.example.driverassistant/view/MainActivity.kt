package com.example.driverassistant.view

import android.Manifest
import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.driverassistant.R
import com.example.driverassistant.controller.MainController
import com.example.driverassistant.model.SensorData
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val mainController = MainController()

    private lateinit var speedCallback: CarPropertyManager.CarPropertyEventCallback
    private lateinit var temperatureCallback: CarPropertyManager.CarPropertyEventCallback

    private lateinit var car: Car
    private lateinit var carPropertyManager: CarPropertyManager
    private val permissions = arrayOf(
        Car.PERMISSION_SPEED,
        Car.PERMISSION_EXTERIOR_ENVIRONMENT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var speedTextView: TextView
    private lateinit var outsideTemperatureTextView: TextView
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var speedLimitTextView: TextView
    private lateinit var drivingSessionScoreTextView: TextView

    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var i = 10
    private var speed = 0
    private var speedLimit = 30
    private var temperature = 0
    private lateinit var currentLocation: Location

    private var sessionStarted = false

    private lateinit var sensorData: SensorData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        speedTextView = findViewById(R.id.speedTextView)
        outsideTemperatureTextView = findViewById(R.id.outsideTemperatureTextView)
        latitudeTextView = findViewById(R.id.latitudeTextView)
        longitudeTextView = findViewById(R.id.longitudeTextView)
        speedLimitTextView = findViewById(R.id.speedLimitTextView)
        drivingSessionScoreTextView = findViewById(R.id.drivingSessionScoreTextView)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)

        initializeCar()

        startButton.setOnClickListener {
            if (!sessionStarted) {
                sessionStarted = true

                Log.d("SESSION", "SESSION HAS STARTED")

                initializeLocation()
                listenSensorData()

                mainController.initializeDrivingSession()
            }
        }

        stopButton.setOnClickListener {
            if (sessionStarted) {
                sessionStarted = false

                Log.d("SESSION", "SESSION HAS STOPPED")

                stopLocationRetrieval()
                stopSensorDataRetrieval()

                mainController.stopDrivingSession()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 1)
            }
        }

        if (sessionStarted) {
            listenSensorData()
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
    }

    private fun listenSensorData() {

        speedCallback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
                speed = (carPropertyValue.value as Float).toInt()
                speedTextView.text = (speed * 3.6).toInt().toString()
            }

            override fun onErrorEvent(p0: Int, p1: Int) {
                "Error retrieving sensor data".also { speedTextView.text = it }
            }
        }

        temperatureCallback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
                temperature = (carPropertyValue.value as Float).toInt()
                outsideTemperatureTextView.text = carPropertyValue.value.toString()
            }

            override fun onErrorEvent(p0: Int, p1: Int) {
                "Error retrieving sensor data".also { outsideTemperatureTextView.text = it }
            }
        }

        carPropertyManager.registerCallback(
            speedCallback,
            VehiclePropertyIds.PERF_VEHICLE_SPEED,
            CarPropertyManager.SENSOR_RATE_NORMAL
        )

        carPropertyManager.registerCallback(
            temperatureCallback,
            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )
    }

    private fun initializeLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(3)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                currentLocation = locationResult.lastLocation
                val latitude = currentLocation.latitude
                val longitude = currentLocation.longitude

                latitudeTextView.text = latitude.toString()
                longitudeTextView.text = longitude.toString()

                val speedLimit = getSpeedLimit(latitude, longitude)
                speedLimitTextView.text = speedLimit.toString()

                sensorData = SensorData(speed, temperature, currentLocation, speedLimit)

                mainController.addSensorData(sensorData)
                val score: Float = mainController.analyzeDrivingSession()

                drivingSessionScoreTextView.text = score.toString()
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationRetrieval() {
        fusedLocationClient.removeLocationUpdates(
            locationCallback
        )
    }

    private fun stopSensorDataRetrieval() {
        carPropertyManager.unregisterCallback(speedCallback)
        carPropertyManager.unregisterCallback(temperatureCallback)
    }

    private fun getSpeedLimit(latitude: Double, longitude: Double): Int {
        val speedLimits = arrayOf(30, 50, 70, 80, 100, 130)

        if (i % 10 == 0) {
            val random = Random.nextInt(5)
            speedLimit = speedLimits.elementAt(random)
        }

        i++

        Log.d("Speed Limit: ", "Retrieving speed limit at $latitude $longitude location " +
                "------> $speedLimit km/h")

        return speedLimit
    }
}
