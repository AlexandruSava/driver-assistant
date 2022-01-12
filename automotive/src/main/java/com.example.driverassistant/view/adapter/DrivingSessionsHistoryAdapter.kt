package com.example.driverassistant.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.DrivingSession

class DrivingSessionsHistoryAdapter(private val drivingSessionsList: MutableList<DrivingSession>, private val onClick: (DrivingSession) -> (Unit)): RecyclerView.Adapter<DrivingSessionsHistoryAdapter.HistoryViewHolder>() {

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        textView1 = holder.itemView.findViewById(R.id.textView10)
        textView2 = holder.itemView.findViewById(R.id.textView11)
        holder.bind(drivingSessionsList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.driving_session_card, parent, false)

        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return drivingSessionsList.size
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
            val realIndex = itemCount - index
            val stringIndex = "#$realIndex Driving Session"
            val score = session.finalScore.toInt()

            textView1.text = stringIndex
            textView2.text = score.toString()

            setScoreTextViewColor(score, textView2)
            index ++
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }

    private fun setScoreTextViewColor(score: Int, textView: TextView) {
        when (score) {
            in 85..100 -> textView.setTextColor(Color.parseColor("#FF4BC100"))
            in 75..84 -> textView.setTextColor(Color.parseColor("#FF64DD17"))
            in 60..74 -> textView.setTextColor(Color.parseColor("#FFE1BC00"))
            in 50..59 -> textView.setTextColor(Color.parseColor("#FFE14F00"))
            in 0..49 -> textView.setTextColor(Color.parseColor("#E10000"))
        }
    }

    companion object {
        var index = 0
    }
}