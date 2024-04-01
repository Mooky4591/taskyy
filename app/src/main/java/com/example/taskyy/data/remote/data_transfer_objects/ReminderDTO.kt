package com.example.taskyy.data.remote.data_transfer_objects

data class ReminderDTO(
    val id: Int,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
)