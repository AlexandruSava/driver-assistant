package com.example.driverassistant.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.database.DatabaseController
import com.example.driverassistant.model.DrivingSession
import com.example.driverassistant.model.Notification
import com.example.driverassistant.view.adapter.DrivingSessionsHistoryAdapter
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class DrivingSessionsHistoryActivity: AppCompatActivity() {
    private val databaseController = DatabaseController()

    private lateinit var userId: String
    private lateinit var email: String

    private lateinit var listAdapterDrivingSessions: DrivingSessionsHistoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.driving_sessions_history_activity)

        initButtons()
        setUserAndEmail()
        getStorageDataAndInitList()
    }

    private fun getStorageDataAndInitList() {
        val initialized = databaseController.verifyPresenceOfALocalFile(this, userId)
        if (initialized) {
            val drivingSessionsList = databaseController.getDrivingSessionsDataFromLocalStorage(this, userId)
            initList(drivingSessionsList)
        }
    }

    private fun initList(drivingSessionsList: ArrayList<DrivingSession>){
        val model: MutableList<DrivingSession> = drivingSessionsList.reversed().toMutableList()

        listAdapterDrivingSessions = DrivingSessionsHistoryAdapter(model) {
            val intent = Intent(this, DetailedDrivingSessionActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            intent.putExtra("index", it.index - 1)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = listAdapterDrivingSessions

    }

    private fun initButtons(){
        imageButton = findViewById(R.id.imageButton4)
        imageButton.setOnClickListener {
            onBackPressed()
            finish()
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
}