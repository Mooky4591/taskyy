package com.example.taskyy.data.local.room_entity

import androidx.room.Entity

@Entity(tableName = "entity_table")
data class TaskEntity(
    val description: String,
    val id: String,
    val isDone: Boolean,
    val remindAt: Long,
    val time: Long,
    val title: String
)