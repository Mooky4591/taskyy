package com.example.taskyy.data.remote.response_objects

data class TaskResponse(
    val id: String,
    val title: String,
    val description: String,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean
)
