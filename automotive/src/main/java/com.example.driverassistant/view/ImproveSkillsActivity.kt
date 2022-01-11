package com.example.driverassistant.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.AdviceModel

class ImproveSkillsActivity: AppCompatActivity() {

    private lateinit var listAdapter: AdviceAdaptor
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.improve_skills)

        initList()
        initButtons()

    }

    private fun initList(){
        val a1 = AdviceModel(
            "Respect Speed Limit",
            "Did you know 90% of accidents happen because of speeding?"
        )
        val a2 = AdviceModel(
            "Slow Down for Crosswalks",
            "More than 70% of victims involved in accidents are pedestrians. " +
                    "They are considered vulnerable road users."
        )
        val a3 = AdviceModel(
            "Pay Attention to Cyclists",
            "Cyclists may be unpredictable and they are also considered vulnerable road" +
                    "users. Slow down when approaching one and leave them enough space."
        )
        val a4 = AdviceModel(
            "Attention at Low Temperatures",
            "Ice can form on the road and drivers sometimes do not realize it. You should" +
                    " drive at least 25% slower than in normal conditions."
        )
        val model: MutableList<AdviceModel> = mutableListOf(a1,a2,a3,a4)

        listAdapter = AdviceAdaptor(model) { }

        recyclerView = findViewById(R.id.notificationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = listAdapter

    }

    private fun initButtons(){
        imageButton = findViewById(R.id.imageButton)
        imageButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}