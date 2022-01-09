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
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.driverassistant.R
import com.example.driverassistant.controller.DrivingSessionController
import com.example.driverassistant.model.SensorData
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DrivingSessionActivity : AppCompatActivity() {
    private val mainController = DrivingSessionController()

    private lateinit var userId: String
    private lateinit var email: String

    private lateinit var speedCallback: CarPropertyManager.CarPropertyEventCallback
    private lateinit var temperatureCallback: CarPropertyManager.CarPropertyEventCallback

    private lateinit var car: Car
    private lateinit var carPropertyManager: CarPropertyManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var scoreTextView: TextView
    private lateinit var stopButton: Button
    private lateinit var notificationListView: ListView

    private var i = 10
    private var speed = 0
    private var speedLimit = 30
    private var temperature = 0
    private lateinit var currentLocation: Location

    private var sessionStarted = false

    private lateinit var sensorData: SensorData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.driving_session_activity)

        setUserAndEmail()
        initializeTextViews()
        initializeButtonsListeners()
        initializeListView()
        initializeCar()
        initializeLocation()
        initializeSensorDataCallbacks()

        startDrivingSession(userId, email)
    }

    private fun initializeListView() {
        notificationListView = findViewById(R.id.notification_list)
    }

    override fun onResume() {
        super.onResume()

        if (sessionStarted) {
            listenSensorDataUpdates()
        }
    }

    override fun onPause() {
        if (car.isConnected) {
            car.disconnect()
        }

        super.onPause()
    }

    private fun setUserAndEmail() {
        val userIdString = intent.getStringExtra("userId")
        val emailString = intent.getStringExtra("email")
        if (!userIdString.isNullOrEmpty() && !emailString.isNullOrEmpty()) {
            userId = userIdString
            email = emailString
        }
        Log.d("SESSION USER:", "$userId $email")
    }

    private fun initializeTextViews() {
        scoreTextView = findViewById(R.id.textView4)
    }

    private fun initializeButtonsListeners() {
        stopButton = findViewById(R.id.button3)

        stopButton.setOnClickListener {
            stopDrivingSession()
        }
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

                val speedLimit = getSpeedLimit(latitude, longitude)

                sensorData = SensorData(speed, temperature, currentLocation, speedLimit)

                mainController.addSensorData(sensorData)
                val score: Float = mainController.analyzeDrivingSession()

                scoreTextView.text = score.toInt().toString()

                val notificationList = mainController.getNotificationList()
            }
        }
    }

    private fun startDrivingSession(userId: String, email: String) {
        sessionStarted = true

        Log.d("SESSION", "SESSION HAS STARTED")

        listenLocationUpdates()
        listenSensorDataUpdates()

        mainController.startDrivingSession(userId, email)
    }

    private fun stopDrivingSession() {
        sessionStarted = false

        Log.d("SESSION", "SESSION HAS STOPPED")

        stopLocationUpdates()
        stopSensorDataUpdates()

        mainController.stopDrivingSession()
        onBackPressed()
    }

    private fun listenSensorDataUpdates() {

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

    private fun initializeSensorDataCallbacks() {
        speedCallback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
                speed = (carPropertyValue.value as Float).toInt()
            }

            override fun onErrorEvent(p0: Int, p1: Int) {
                println("Error retrieving sensor data")
            }
        }

        temperatureCallback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
                temperature = (carPropertyValue.value as Float).toInt()
            }

            override fun onErrorEvent(p0: Int, p1: Int) {
                println("Error retrieving sensor data")
            }
        }
    }

    private fun listenLocationUpdates() {
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

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(
            locationCallback
        )
    }

    private fun stopSensorDataUpdates() {
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

        return speedLimit
    }
}
