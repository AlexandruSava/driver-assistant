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
        val a1 = AdviceModel("Ana", "descriere de test")
        val a2 = AdviceModel("Ana", "descriere de test")
        val a3 = AdviceModel("Ana", "descriere de test")
        val a4 = AdviceModel("Ana", "descriere de test")
        val model: MutableList<AdviceModel> = mutableListOf(a1,a2,a3,a4)

        listAdapter = AdviceAdaptor(model){
            System.out.println("ceva")
        }

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