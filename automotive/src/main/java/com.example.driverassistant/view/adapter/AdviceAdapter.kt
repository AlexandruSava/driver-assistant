package com.example.driverassistant.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.Advice


class AdviceAdapter(private val adviceList: MutableList<Advice>, private val onClick: (Advice) -> (Unit)): RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder>() {

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {

        textView1 = holder.itemView.findViewById(R.id.textView7)
        textView2 = holder.itemView.findViewById(R.id.textView2)
        holder.bind(adviceList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_card, parent, false)

        return AdviceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return adviceList.size
    }

    inner class AdviceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init{
            itemView.setOnClickListener { }
        }

        fun bind(advice: Advice){
            textView1.text = advice.title
            textView2.text = advice.description
        }
    }
}