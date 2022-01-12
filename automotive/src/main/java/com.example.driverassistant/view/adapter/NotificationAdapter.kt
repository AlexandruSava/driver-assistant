package com.example.driverassistant.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.Notification

class NotificationAdapter(private val notifList: MutableList<Notification>, private val onClick: (Notification) -> (Unit)): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        textView1 = holder.itemView.findViewById(R.id.textView7)
        textView2 = holder.itemView.findViewById(R.id.textView2)
        holder.bind(notifList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_card, parent, false)

        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notifList.size
    }

    inner class NotificationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init{
            itemView.setOnClickListener { }
        }

        fun bind(notification: Notification){
            textView1.text = notification.title
            textView2.text = notification.message
        }
    }
}