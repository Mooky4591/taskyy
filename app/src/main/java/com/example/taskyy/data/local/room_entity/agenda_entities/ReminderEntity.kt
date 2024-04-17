package com.example.taskyy.data.local.room_entity.agenda_entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskyy.data.remote.data_transfer_objects.ReminderDTO
import com.example.taskyy.ui.enums.AgendaItemAction
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.objects.Reminder

@Entity(tableName = "reminder_table")
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val description: String,
    val remindAt: Long,
    val time: Long,
    val title: String,
    val eventType: AgendaItemType,
    val action: AgendaItemAction
) {
    fun transformToReminder(): Reminder {
        return Reminder(
            title,
            description,
            time,
            remindAt,
            id,
            eventType,
            action
        )
    }

    fun toReminderDTO(): ReminderDTO {
        return ReminderDTO(
            id = id,
            remindAt = remindAt,
            description = description,
            title = title,
            time = time
        )
    }

}