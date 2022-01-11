package com.example.driverassistant.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.DrivingSession

class DrivingSessionsHistoryAdapter(private val sessionsList: MutableList<DrivingSession>, private val onClick: (DrivingSession) -> (Unit)): RecyclerView.Adapter<DrivingSessionsHistoryAdapter.HistoryViewHolder>() {

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        textView1 = holder.itemView.findViewById(R.id.textView10)
        textView2 = holder.itemView.findViewById(R.id.textView11)
        holder.bind(sessionsList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.driving_session_card, parent, false)

        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sessionsList.size
    }

    lateinit var mClickListener: ClickListener

    fun setOnClickListener(aClickListener: ClickListener){
        mClickListener = aClickListener
    }

    interface ClickListener{
        fun onClick(pos: Int, aView: View)
    }

    inner class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init{
            itemView.setOnClickListener(this)
        }

        fun bind(session: DrivingSession){
            textView1.text = session.startTime.toString()
            textView2.text = session.finalScore.toString()
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }
}