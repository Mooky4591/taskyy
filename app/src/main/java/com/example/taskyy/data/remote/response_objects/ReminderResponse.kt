package com.example.taskyy.data.remote.response_objects

data class ReminderResponse(
    val id: String,
    val title: String,
    val description: String,
    val time: Long,
    val remindAt: Long,
)
