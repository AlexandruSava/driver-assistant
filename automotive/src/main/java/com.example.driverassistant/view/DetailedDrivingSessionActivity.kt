package com.example.driverassistant.view

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.Notification

class DetailedDrivingSessionActivity: AppCompatActivity() {
    private lateinit var listAdapter: NotificationAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_driving_session_activity)

        initList()
        initButtons()

    }

    private fun initList(){
        val n1 = Notification("hei", "ceva text", "mesaj",12,12)
        val n2 = Notification("hei", "ceva text", "mesaj",12,12)
        val model: MutableList<Notification> = mutableListOf(n1,n2)

        listAdapter = NotificationAdapter(model){
            System.out.println("ceva")
        }

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
}