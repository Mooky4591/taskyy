package com.example.taskyy.ui.objects

import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity
import com.example.taskyy.data.local.room_entity.pending_agenda_retry.PendingReminderRetryEntity
import com.example.taskyy.data.remote.data_transfer_objects.ReminderDTO
import com.example.taskyy.ui.enums.AgendaItemAction
import com.example.taskyy.ui.enums.AgendaItemType

class Reminder(
    title: String,
    description: String,
    timeInMillis: Long,
    alarmType: Long,
    id: String,
    agendaItem: AgendaItemType,
    agendaAction: AgendaItemAction
) : AgendaEventItem(title, description, alarmType, id, timeInMillis, agendaItem, agendaAction) {

    override fun toReminderEntity(): ReminderEntity {
        return ReminderEntity(
            id = eventId,
            description = description,
            title = title,
            remindAt = alarmType,
            time = timeInMillis,
            action = agendaAction
        )
    }

    fun toReminderDto(): ReminderDTO {
        return ReminderDTO(
            id = eventId,
            description = description,
            title = title,
            time = timeInMillis,
            remindAt = alarmType
        )
    }

    fun toPendingReminderRetryEntity(): PendingReminderRetryEntity {
        return PendingReminderRetryEntity(
            id = eventId,
            action = agendaAction,
        )
    }
}
