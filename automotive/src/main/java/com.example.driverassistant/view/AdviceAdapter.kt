package com.example.driverassistant.view

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

    lateinit var mClickListener: ClickListener

    fun setOnClickListener(aClickListener: ClickListener){
        mClickListener = aClickListener
    }

    interface ClickListener{
        fun onClick(pos: Int, aView: View)
    }

    inner class AdviceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init{
            itemView.setOnClickListener(this)
        }

        fun bind(advice: Advice){
            textView1.text = advice.title
            textView2.text = advice.description
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }
}