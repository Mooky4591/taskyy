package com.example.taskyy.data.local.room_entity.pending_agenda_retry

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskyy.ui.enums.AgendaItemAction

@Entity(tableName = "pending_event_retry_table")
data class PendingEventRetryEntity(
    @PrimaryKey
    val id: String,
    val action: AgendaItemAction

)
