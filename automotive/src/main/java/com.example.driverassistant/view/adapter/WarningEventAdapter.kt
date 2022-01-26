package com.example.driverassistant.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.Notification
import com.example.driverassistant.model.WarningEvent

class WarningEventAdapter(
    private val warningEventsList: MutableList<WarningEvent>
) : RecyclerView.Adapter<WarningEventAdapter.WarningEventViewHolder>() {

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    override fun onBindViewHolder(holder: WarningEventViewHolder, position: Int) {

        textView1 = holder.itemView.findViewById(R.id.textView7)
        textView2 = holder.itemView.findViewById(R.id.textView2)
        holder.bind(warningEventsList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarningEventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.warning_event_card, parent, false)

        return WarningEventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return warningEventsList.size
    }

    inner class WarningEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { }
        }

        fun bind(warningEvent: WarningEvent) {
            var title = "Error"
            var message = "Error"
            when (warningEvent.type) {
                "good_driving" -> {
                    title = Notification.GOOD_DRIVING.title
                    message = Notification.GOOD_DRIVING.message
                }
                "speeding" -> {
                    title = Notification.SPEEDING.title
                    message = Notification.SPEEDING.message
                }
            }
            textView1.text = title
            textView2.text = message
        }
    }
}