package com.example.driverassistant.view

import android.Manifest
import android.car.Car
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.driverassistant.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity(){

    private lateinit var userId: String
    private lateinit var email: String

    private lateinit var welcomeTextView: TextView
    private lateinit var startSessionButton: Button
    private lateinit var logoutButton: Button

    private lateinit var improveSkillsButton: ImageButton
    private lateinit var historyButton: ImageButton

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
        logoutButton = findViewById(R.id.logout_btn)
        improveSkillsButton = findViewById(R.id.improve_skills_btn)
        historyButton = findViewById(R.id.imageButton2)

        startSessionButton.setOnClickListener {
            val intent = Intent(this, DrivingSessionActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        logoutButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            Firebase.auth.signOut()
            startActivity(intent)
            finish()
        }

        improveSkillsButton.setOnClickListener{
            val intent = Intent(this, ImproveSkillsActivity::class.java)
            startActivity(intent)
        }

        historyButton.setOnClickListener{
            val intent = Intent(this, SessionsHistoryActivity::class.java)
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