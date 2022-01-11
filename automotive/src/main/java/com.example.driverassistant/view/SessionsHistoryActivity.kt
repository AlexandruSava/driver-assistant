package com.example.driverassistant.view

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.AdviceModel
import com.example.driverassistant.model.DrivingSession
import com.example.driverassistant.model.Notification

class SessionsHistoryActivity: AppCompatActivity() {
    private lateinit var listAdapter: HistoryAdaptor
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.driving_history)

        initList()
        initButtons()

    }

    private fun initList(){
        val n1 = Notification("hei", "ceva text", "mesaj",12,12)
        val n2 = Notification("hei", "ceva text", "mesaj",12,12)
        val arrayList: ArrayList<Notification> = ArrayList()
        arrayList.add(n1)
        arrayList.add(n2)

        val a1 = DrivingSession("Ana", "descriere de test", 12, 23,20F, 40F, arrayList)
        val a2 = DrivingSession("Ana", "descriere de test", 12, 23,20F, 40F, arrayList)
        val a3 = DrivingSession("Ana", "descriere de test", 12, 23,20F, 40F, arrayList)
        val a4 = DrivingSession("Ana", "descriere de test", 12, 23,20F, 40F, arrayList)
        val model: MutableList<DrivingSession> = mutableListOf(a1,a2,a3,a4)

        listAdapter = HistoryAdaptor(model){
            System.out.println("ceva")
        }

        recyclerView = findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = listAdapter

    }

    private fun initButtons(){
        imageButton = findViewById(R.id.imageButton4)
        imageButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}