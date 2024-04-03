package com.example.taskyy.data.local.room_entity.agenda_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    //val attendees: List<AttendeeEntity>,
    val description: String,
    val from: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    //val photo: List<Photo>,
    val remindAt: Long,
    val title: String,
    val to: Long
)