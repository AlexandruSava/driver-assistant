package com.example.driverassistant.view.activity

import android.Manifest
import android.car.Car
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.driverassistant.R
import com.example.driverassistant.controller.DashboardController
import com.example.driverassistant.database.DatabaseController
import com.example.driverassistant.database.FirebaseController
import com.example.driverassistant.model.DrivingSession
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {

    private val dashboardController = DashboardController()
    private val databaseController = DatabaseController()
    private val firebaseController = FirebaseController()

    private lateinit var userId: String
    private lateinit var email: String

    private lateinit var welcomeTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var startSessionButton: Button
    private lateinit var logoutButton: Button

    private lateinit var improveSkillsButton: ImageButton
    private lateinit var historyButton: ImageButton

    private lateinit var horizontalLineImageView: ImageView

    private val database =
        Firebase.database("https://driver-assistant-f8cd9-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("driving_sessions")

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
        initializeImageViews()
        getDataFromFirebase()
    }

    override fun onResume() {
        super.onResume()
        getStorageData()
    }

    private fun initializeImageViews() {
        horizontalLineImageView = findViewById(R.id.imageView5)
    }

    private fun getDataFromFirebase() {
        val reference = database.child(userId)
        val context = this

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                firebaseController.getFirebaseDataAndWriteDrivingSessionsDataInLocalStorage(
                    snapshot,
                    userId,
                    context
                )
                getStorageData()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Database Error!")
            }
        })
    }

    private fun getStorageData() {
        val initialized = databaseController.verifyPresenceOfALocalFile(this, userId)
        if (initialized) {
            val drivingSessionsList =
                databaseController.getDrivingSessionsDataFromLocalStorage(this, userId)
            if (drivingSessionsList.isNotEmpty()) {
                scoreTextView.visibility = View.VISIBLE
                horizontalLineImageView.visibility = View.INVISIBLE
                setAverageScore(drivingSessionsList)
            } else {
                scoreTextView.visibility = View.INVISIBLE
                horizontalLineImageView.visibility = View.VISIBLE
            }
        } else {
            scoreTextView.visibility = View.INVISIBLE
            horizontalLineImageView.visibility = View.VISIBLE
        }
    }

    private fun setAverageScore(drivingSessionsList: ArrayList<DrivingSession>) {
        val score = dashboardController.calculateUserScore(drivingSessionsList)
        scoreTextView.text = score.toString()
        setScoreTextViewColor(score)
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

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            Firebase.auth.signOut()
            startActivity(intent)
            finish()
        }

        improveSkillsButton.setOnClickListener {
            val intent = Intent(this, ImproveDrivingSkillsActivity::class.java)
            startActivity(intent)
        }

        historyButton.setOnClickListener {
            val intent = Intent(this, DrivingSessionsHistoryActivity::class.java)
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
        scoreTextView = findViewById(R.id.textView4)
        val welcomeMessage = "Welcome $email"
        welcomeTextView.text = welcomeMessage
        scoreTextView.text = 100.toString()
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

    private fun setScoreTextViewColor(score: Int) {
        when (score) {
            in 85..100 -> scoreTextView.setTextColor(Color.parseColor("#FF4BC100"))
            in 75..84 -> scoreTextView.setTextColor(Color.parseColor("#FF64DD17"))
            in 60..74 -> scoreTextView.setTextColor(Color.parseColor("#FFE1BC00"))
            in 50..59 -> scoreTextView.setTextColor(Color.parseColor("#FFE14F00"))
            in 0..49 -> scoreTextView.setTextColor(Color.parseColor("#E10000"))
        }
    }

}