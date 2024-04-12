package com.example.taskyy.data.local.room_entity.agenda_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity_table")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val description: String,
    val isDone: Boolean,
    val remindAt: Long,
    val time: Long,
    val title: String
)