package com.example.driverassistant.view.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.database.DatabaseController
import com.example.driverassistant.model.DrivingSession
import com.example.driverassistant.model.Notification
import com.example.driverassistant.view.adapter.NotificationAdapter

class DetailedDrivingSessionActivity: AppCompatActivity() {
    private val databaseController = DatabaseController()

    private lateinit var userId: String
    private lateinit var email: String
    private var index = 0

    private lateinit var listAdapter: NotificationAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageButton: ImageButton

    private lateinit var drivingSessionTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var averageSpeedTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_driving_session_activity)

        initTextViews()
        initButtons()
        setUserAndEmail()
        getStorageDataAndSetListDataAndTextViews()
    }

    private fun initTextViews() {
        drivingSessionTextView = findViewById(R.id.textView13)
        scoreTextView = findViewById(R.id.textView15)
        averageSpeedTextView = findViewById(R.id.textView17)
    }

    private fun initList(drivingSession: DrivingSession){
        val model: MutableList<Notification> = drivingSession.notificationsList.reversed().toMutableList()

        listAdapter = NotificationAdapter(model) { }

        recyclerView = findViewById(R.id.detailsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = listAdapter

    }

    private fun initButtons(){
        imageButton = findViewById(R.id.imageButton5)
        imageButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun getStorageDataAndSetListDataAndTextViews() {
        val initialized = databaseController.verifyPresenceOfALocalFile(this, userId)
        if (initialized) {
            val drivingSession =
                databaseController
                    .getDriverSessionBySessionIndex(
                        this,
                        userId,
                        index
                    )
            initList(drivingSession)

            val drivingSessionTitle = "#$index Driving Session"
            val score = drivingSession.finalScore.toInt()
            val averageSpeed = (drivingSession.averageSpeed * 3.6).toInt().toString() + " km/h"
            drivingSessionTextView.text = drivingSessionTitle
            averageSpeedTextView.text = averageSpeed
            scoreTextView.text = score.toString()
            setScoreTextViewColor(score)
        }
    }

    private fun setUserAndEmail() {
        val userIdString = intent.getStringExtra("userId")
        val emailString = intent.getStringExtra("email")
        val indexInt = intent.getIntExtra("index", 0)
        if (!userIdString.isNullOrEmpty() && !emailString.isNullOrEmpty()) {
            userId = userIdString
            email = emailString
            index = indexInt
        }
        Log.d("SESSION USER & DRIVING SESSION:", "$userId $email Driving session: $index")
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