package com.example.taskyy.ui.objects

import com.example.taskyy.data.local.room_entity.agenda_entities.TaskEntity
import com.example.taskyy.data.local.room_entity.pending_agenda_retry.PendingTaskRetryEntity
import com.example.taskyy.data.remote.data_transfer_objects.TaskDTO
import com.example.taskyy.ui.enums.AgendaItemAction
import com.example.taskyy.ui.enums.AgendaItemType

class Task(
    title: String,
    description: String,
    timeInMillis: Long,
    alarmType: Long,
    id: String,
    var isDone: Boolean,
    agendaItem: AgendaItemType,
    agendaAction: AgendaItemAction
) : AgendaEventItem(title, description, alarmType, id, timeInMillis, agendaItem, agendaAction) {
    fun toTaskEntity(): TaskEntity {
        return TaskEntity(
            isDone = isDone,
            time = timeInMillis,
            action = agendaAction,
            description = description,
            id = eventId,
            title = title,
            remindAt = alarmType,
            eventType = eventType
        )
    }

    fun toPendingTaskRetryEntity(): PendingTaskRetryEntity {
        return PendingTaskRetryEntity(
            id = eventId,
            action = agendaAction,
        )
    }

    fun toTaskDTO(): TaskDTO {
        return TaskDTO(
            id = eventId,
            title = title,
            description = description,
            time = timeInMillis,
            remindAt = alarmType
        )
    }
}

