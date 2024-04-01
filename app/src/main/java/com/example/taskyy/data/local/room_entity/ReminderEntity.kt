package com.example.taskyy.data.local.room_entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class ReminderEntity(
    @PrimaryKey
    val id: Int,
    val description: String,
    val remindAt: Long,
    val time: Long,
    val title: String
)