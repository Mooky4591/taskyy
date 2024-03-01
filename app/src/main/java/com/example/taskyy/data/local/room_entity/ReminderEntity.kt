package com.example.taskyy.data.local.room_entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val description: String?,
    val remindAt: Long,
    val time: Long,
    val title: String
)