package com.example.driverassistant.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.AdviceModel
import kotlinx.android.synthetic.main.improve_skills.*

class ImproveSkillsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.improve_skills)

        val a1 = AdviceModel("Ana", "descriere de test")
        val a2 = AdviceModel("Ana", "descriere de test")
        val a3 = AdviceModel("Ana", "descriere de test")
        val a4 = AdviceModel("Ana", "descriere de test")
        val model: MutableList<AdviceModel> = mutableListOf(a1,a2,a3,a4)

        val adapter = AdviceAdaptor(model, this)

        notifRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        notifRecyclerView.adapter = adapter

    }
}