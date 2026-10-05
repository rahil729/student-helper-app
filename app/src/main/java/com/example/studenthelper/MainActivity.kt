package com.example.studenthelper

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Student Helper App"

        findViewById<MaterialCardView>(R.id.timetableCard).setOnClickListener {
            startActivity(Intent(this, TimetableActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.examsCard).setOnClickListener {
            startActivity(Intent(this, ExamsActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.remindersCard).setOnClickListener {
            startActivity(Intent(this, RemindersActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        updateSavedCounts()
    }

    private fun updateSavedCounts() {
        val preferences = getSharedPreferences("student_helper_prefs", MODE_PRIVATE)

        val classCount = JSONArray(
            preferences.getString("timetable_items", "[]") ?: "[]"
        ).length()
        val examCount = JSONArray(
            preferences.getString("exam_items", "[]") ?: "[]"
        ).length()
        val reminderCount = JSONArray(
            preferences.getString("reminder_items", "[]") ?: "[]"
        ).length()

        findViewById<TextView>(R.id.timetableCount).text =
            resources.getQuantityString(R.plurals.classes_saved, classCount, classCount)
        findViewById<TextView>(R.id.examCount).text =
            resources.getQuantityString(R.plurals.exams_tracked, examCount, examCount)
        findViewById<TextView>(R.id.reminderCount).text =
            resources.getQuantityString(R.plurals.reminders_saved, reminderCount, reminderCount)
    }
}
