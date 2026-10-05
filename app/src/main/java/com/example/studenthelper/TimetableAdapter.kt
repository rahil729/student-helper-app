package com.example.studenthelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimetableAdapter(
    private val items: MutableList<TimetableItem>
) : RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    class TimetableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectText: TextView = itemView.findViewById(R.id.timetableSubject)
        val dayTimeText: TextView = itemView.findViewById(R.id.timetableDayTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timetable, parent, false)
        return TimetableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val item = items[position]
        holder.subjectText.text = item.subject
        holder.dayTimeText.text = holder.itemView.context.getString(
            R.string.class_details,
            item.day,
            item.time
        )
    }

    override fun getItemCount(): Int = items.size
}
