package com.example.driverassistant.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.Notification

class NotificationAdapter(private val notifList: MutableList<Notification>, private val onClick: (Notification) -> (Unit)): RecyclerView.Adapter<NotificationAdapter.NotifViewHolder>() {

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {

        textView1 = holder.itemView.findViewById(R.id.textView7)
        textView2 = holder.itemView.findViewById(R.id.textView2)
        holder.bind(notifList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_card, parent, false)

        return NotifViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notifList.size
    }

    lateinit var mClickListener: ClickListener

    fun setOnClickListener(aClickListener: ClickListener){
        mClickListener = aClickListener
    }

    interface ClickListener{
        fun onClick(pos: Int, aView: View)
    }

    inner class NotifViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init{
            itemView.setOnClickListener(this)
        }

        fun bind(notif: Notification){
            textView1.text = notif.title
            textView2.text = notif.message
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }
}