package com.example.taskyy.data.local.room_entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val description: String,
    val isDone: Boolean,
    val remindAt: Long,
    val time: Long,
    val title: String
)