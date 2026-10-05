package com.example.studenthelper

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class TimetableActivity : AppCompatActivity() {

    private lateinit var adapter: TimetableAdapter
    private val items = mutableListOf<TimetableItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)
        title = "Level 2 • Timetable"

        val recyclerView = findViewById<RecyclerView>(R.id.timetableRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TimetableAdapter(items)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.addSubjectButton).setOnClickListener {
            addSubject()
        }

        loadItems()
        updateEmptyState()
    }

    private fun addSubject() {
        val subjectInput = findViewById<EditText>(R.id.subjectInput)
        val timeInput = findViewById<EditText>(R.id.timeInput)
        val daySpinner = findViewById<Spinner>(R.id.daySpinner)

        val subject = subjectInput.text.toString().trim()
        val time = timeInput.text.toString().trim()
        val day = daySpinner.selectedItem?.toString() ?: "Monday"

        if (subject.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please enter a subject and time", Toast.LENGTH_SHORT).show()
            return
        }

        items.add(TimetableItem(subject, day, time))
        saveItems()
        adapter.notifyItemInserted(items.size - 1)
        subjectInput.text.clear()
        timeInput.text.clear()
        updateEmptyState()
    }

    private fun saveItems() {
        val array = JSONArray()
        items.forEach { item ->
            val objectItem = JSONObject()
            objectItem.put("subject", item.subject)
            objectItem.put("day", item.day)
            objectItem.put("time", item.time)
            array.put(objectItem)
        }

        getSharedPreferences("student_helper_prefs", MODE_PRIVATE)
            .edit()
            .putString("timetable_items", array.toString())
            .apply()
    }

    private fun loadItems() {
        items.clear()
        val prefs = getSharedPreferences("student_helper_prefs", MODE_PRIVATE)
        val raw = prefs.getString("timetable_items", "[]") ?: "[]"
        val array = JSONArray(raw)

        for (index in 0 until array.length()) {
            val objectItem = array.getJSONObject(index)
            items.add(
                TimetableItem(
                    subject = objectItem.getString("subject"),
                    day = objectItem.getString("day"),
                    time = objectItem.getString("time")
                )
            )
        }
    }

    private fun updateEmptyState() {
        val emptyText = findViewById<TextView>(R.id.noTimetableItems)
        emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }
}
