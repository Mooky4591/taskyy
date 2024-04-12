package com.example.taskyy.data.local.room_entity.agenda_entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskyy.ui.enums.AgendaItemAction

@Entity(tableName = "reminder_table")
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val description: String,
    val remindAt: Long,
    val time: Long,
    val title: String,
    val action: AgendaItemAction
)