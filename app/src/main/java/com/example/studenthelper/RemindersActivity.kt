package com.example.studenthelper

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RemindersActivity : AppCompatActivity() {

    private lateinit var adapter: ReminderAdapter
    private val items = mutableListOf<ReminderItem>()
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)
        title = "Level 4 • Reminders"

        val recyclerView = findViewById<RecyclerView>(R.id.remindersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ReminderAdapter(
            items = items,
            onDelete = { position ->
                items.removeAt(position)
                saveItems()
                adapter.notifyItemRemoved(position)
                updateEmptyState()
            },
            onCompletionChanged = { position, completed ->
                items[position] = items[position].copy(completed = completed)
                saveItems()
                adapter.notifyItemChanged(position)
            }
        )
        recyclerView.adapter = adapter

        val reminderDateInput = findViewById<EditText>(R.id.reminderDateInput)
        reminderDateInput.setOnClickListener {
            showDatePicker(reminderDateInput)
        }

        findViewById<Button>(R.id.addReminderButton).setOnClickListener {
            addReminder()
        }

        loadItems()
        updateEmptyState()
    }

    private fun showDatePicker(dateField: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }.time
            selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
            dateField.setText(selectedDate)
        }, year, month, day).show()
    }

    private fun addReminder() {
        val reminderTitleInput = findViewById<EditText>(R.id.reminderTitleInput)
        val reminderDateInput = findViewById<EditText>(R.id.reminderDateInput)

        val title = reminderTitleInput.text.toString().trim()
        val date = selectedDate ?: reminderDateInput.text.toString().trim()

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please enter reminder title and date", Toast.LENGTH_SHORT).show()
            return
        }

        val reminder = ReminderItem(title, date)
        val insertAt = items.indexOfFirst { parseDate(it.date).after(parseDate(date)) }
            .let { if (it == -1) items.size else it }
        items.add(insertAt, reminder)
        saveItems()
        adapter.notifyItemInserted(insertAt)
        reminderTitleInput.text.clear()
        reminderDateInput.text.clear()
        selectedDate = null
        updateEmptyState()
    }

    private fun saveItems() {
        val array = JSONArray()
        items.forEach { item ->
            val objectItem = JSONObject()
            objectItem.put("title", item.title)
            objectItem.put("date", item.date)
            objectItem.put("completed", item.completed)
            array.put(objectItem)
        }

        getSharedPreferences("student_helper_prefs", MODE_PRIVATE)
            .edit()
            .putString("reminder_items", array.toString())
            .apply()
    }

    private fun loadItems() {
        items.clear()
        val prefs = getSharedPreferences("student_helper_prefs", MODE_PRIVATE)
        val raw = prefs.getString("reminder_items", "[]") ?: "[]"
        val array = JSONArray(raw)

        for (index in 0 until array.length()) {
            val objectItem = array.getJSONObject(index)
            items.add(
                ReminderItem(
                    title = objectItem.getString("title"),
                    date = objectItem.getString("date"),
                    completed = objectItem.optBoolean("completed", false)
                )
            )
        }
        items.sortBy { parseDate(it.date) }
    }

    private fun parseDate(dateString: String): Date {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString) ?: Date(0)
    }

    private fun updateEmptyState() {
        val emptyText = findViewById<TextView>(R.id.noRemindersText)
        emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }
}
