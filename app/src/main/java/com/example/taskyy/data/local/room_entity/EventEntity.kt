package com.example.taskyy.data.local.room_entity

import androidx.room.Entity

@Entity(tableName = "event_table")
data class EventEntity(
    //val attendees: List<AttendeeEntity>,
    val description: String,
    val from: Long,
    val host: String,
    val id: String,
    val isUserEventCreator: Boolean,
    //val photo: List<Photo>,
    val remindAt: Long,
    val title: String,
    val to: Long
)