package com.example.driverassistant.view.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.Advice
import com.example.driverassistant.view.adapter.AdviceAdapter

class ImproveDrivingSkillsActivity: AppCompatActivity() {

    private lateinit var listAdapter: AdviceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.improve_driving_skills_activity)

        initList()
        initButtons()

    }

    private fun initList(){
        val a1 = Advice(
            "Respect Speed Limit",
            "Did you know 90% of accidents happen because of speeding?"
        )
        val a2 = Advice(
            "Slow Down for Crosswalks",
            "More than 70% of victims involved in accidents are pedestrians. " +
                    "They are considered vulnerable road users."
        )
        val a3 = Advice(
            "Pay Attention to Cyclists",
            "Cyclists may be unpredictable and they are also considered vulnerable road" +
                    "users. Slow down when approaching one and leave them enough space."
        )
        val a4 = Advice(
            "Attention at Low Temperatures",
            "Ice can form on the road and drivers sometimes do not realize it. You should" +
                    " drive at least 25% slower than in normal conditions."
        )
        val model: MutableList<Advice> = mutableListOf(a1,a2,a3,a4)

        listAdapter = AdviceAdapter(model) { }

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