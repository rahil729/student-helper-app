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

class ExamsActivity : AppCompatActivity() {

    private lateinit var adapter: ExamAdapter
    private val items = mutableListOf<ExamItem>()
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exams)
        title = "Level 3 • Exam Schedule"

        val recyclerView = findViewById<RecyclerView>(R.id.examsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExamAdapter(items)
        recyclerView.adapter = adapter

        val examDateInput = findViewById<EditText>(R.id.examDateInput)
        examDateInput.setOnClickListener {
            showDatePicker(examDateInput)
        }

        findViewById<Button>(R.id.addExamButton).setOnClickListener {
            addExam()
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

    private fun addExam() {
        val examNameInput = findViewById<EditText>(R.id.examNameInput)
        val examDateInput = findViewById<EditText>(R.id.examDateInput)

        val subject = examNameInput.text.toString().trim()
        val date = selectedDate ?: examDateInput.text.toString().trim()

        if (subject.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please enter exam subject and date", Toast.LENGTH_SHORT).show()
            return
        }

        val exam = ExamItem(subject, date)
        val insertAt = items.indexOfFirst { parseDate(it.date).after(parseDate(date)) }
            .let { if (it == -1) items.size else it }
        items.add(insertAt, exam)
        saveItems()
        adapter.notifyItemInserted(insertAt)
        examNameInput.text.clear()
        examDateInput.text.clear()
        selectedDate = null
        updateEmptyState()
    }

    private fun saveItems() {
        val array = JSONArray()
        items.forEach { item ->
            val objectItem = JSONObject()
            objectItem.put("subject", item.subject)
            objectItem.put("date", item.date)
            array.put(objectItem)
        }

        getSharedPreferences("student_helper_prefs", MODE_PRIVATE)
            .edit()
            .putString("exam_items", array.toString())
            .apply()
    }

    private fun loadItems() {
        items.clear()
        val prefs = getSharedPreferences("student_helper_prefs", MODE_PRIVATE)
        val raw = prefs.getString("exam_items", "[]") ?: "[]"
        val array = JSONArray(raw)

        for (index in 0 until array.length()) {
            val objectItem = array.getJSONObject(index)
            items.add(
                ExamItem(
                    subject = objectItem.getString("subject"),
                    date = objectItem.getString("date")
                )
            )
        }
        items.sortBy { parseDate(it.date) }
    }

    private fun parseDate(dateString: String): Date {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString) ?: Date(0)
    }

    private fun updateEmptyState() {
        val emptyText = findViewById<TextView>(R.id.noExamsText)
        emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }
}
