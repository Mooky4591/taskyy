package com.example.taskyy.ui.objects

class Reminder(
    title: String,
    description: String,
    val timeInMillis: Long,
    alarmType: Long,
    val id: String,
    val userId: String
) : AgendaEventItem(title, description, alarmType, eventId = id) {

}
