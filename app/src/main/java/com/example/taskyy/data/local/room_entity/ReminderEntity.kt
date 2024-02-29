package com.example.taskyy.data.local.room_entity

import androidx.room.Entity

@Entity
data class ReminderEntity(
    val description: String?,
    val id: String,
    val remindAt: Long,
    val time: Long,
    val title: String
)