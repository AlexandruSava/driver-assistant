package com.example.driverassistant.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.driverassistant.R
import com.example.driverassistant.model.AdviceModel
import kotlinx.android.synthetic.main.notification.view.*

class AdviceAdaptor(val adviceList: List<AdviceModel>, val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(adviceList.get(position));
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.notification, parent, false))
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

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init{
            itemView.setOnClickListener(this)
        }

        fun bind(advice: AdviceModel): Unit{
            itemView.textView7.text = advice.title
            itemView.textView2.text = advice.description
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }
}