package com.example.taskyy.ui.objects

class Reminder(
    title: String,
    description: String,
    val timeInMillis: Long,
    alarmType: Long,
    val id: Int
) : AgendaEventItem(title, description, alarmType) {

}
