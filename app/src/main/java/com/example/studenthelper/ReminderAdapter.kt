package com.example.studenthelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReminderAdapter(
    private val items: MutableList<ReminderItem>,
    private val onDelete: (Int) -> Unit,
    private val onCompletionChanged: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.reminderTitle)
        val dateText: TextView = itemView.findViewById(R.id.reminderDate)
        val completedCheckBox: CheckBox = itemView.findViewById(R.id.reminderCompleted)
        val deleteButton: Button = itemView.findViewById(R.id.deleteReminderButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val item = items[position]
        holder.titleText.text = item.title
        holder.dateText.text = item.date
        holder.completedCheckBox.setOnCheckedChangeListener(null)
        holder.completedCheckBox.isChecked = item.completed
        holder.deleteButton.isEnabled = item.completed
        holder.completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                onCompletionChanged(currentPosition, isChecked)
            }
        }
        holder.deleteButton.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                onDelete(currentPosition)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
