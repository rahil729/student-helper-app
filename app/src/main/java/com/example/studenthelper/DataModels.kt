package com.example.studenthelper

data class TimetableItem(
    val subject: String,
    val day: String,
    val time: String
)

data class ExamItem(
    val subject: String,
    val date: String
)

data class ReminderItem(
    val title: String,
    val date: String,
    val completed: Boolean = false
)
