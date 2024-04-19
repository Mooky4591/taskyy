package com.example.taskyy.data.remote.response_objects


data class AgendaResponse(
    val events: List<EventResponse>,
    val tasks: List<TaskResponse>,
    val reminders: List<ReminderResponse>
) {
}