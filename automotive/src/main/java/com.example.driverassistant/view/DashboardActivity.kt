package com.example.driverassistant.view

import android.Manifest
import android.car.Car
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.driverassistant.R

class DashboardActivity : AppCompatActivity(){

    private lateinit var userId: String
    private lateinit var email: String

    private lateinit var welcomeTextView: TextView
    private lateinit var startSessionButton: Button

    private val permissions = arrayOf(
        Car.PERMISSION_SPEED,
        Car.PERMISSION_EXTERIOR_ENVIRONMENT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)

        requestPermissions()
        setUserAndEmail()
        initializeTextViews()
        initializeButtons()
    }

    private fun initializeButtons() {
        startSessionButton = findViewById(R.id.button3)

        startSessionButton.setOnClickListener {
            val intent = Intent(this, DrivingSessionActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            startActivity(intent)
        }
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
        welcomeTextView = findViewById(R.id.textView6)
        val welcomeMessage = "Welcome $email"
        welcomeTextView.text = welcomeMessage
    }

    private fun requestPermissions() {
        val requestMultiplePermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    Log.e("DEBUG", "${it.key} = ${it.value}")
                }
            }

        requestMultiplePermission.launch(permissions)
    }

}