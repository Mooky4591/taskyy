package com.example.taskyy.ui.objects

import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.TaskEntity
import com.example.taskyy.data.local.room_entity.pending_agenda_retry.PendingReminderRetryEntity
import com.example.taskyy.data.local.room_entity.pending_agenda_retry.PendingTaskRetryEntity
import com.example.taskyy.ui.enums.AgendaItemAction
import com.example.taskyy.ui.enums.AgendaItemType

open class AgendaEventItem(
    val title: String,
    val description: String,
    val alarmType: Long,
    val eventId: String,
    val timeInMillis: Long,
    val eventType: AgendaItemType,
    var agendaAction: AgendaItemAction
) {

    fun toPendingRetryEntity(agendaItemType: AgendaItemType): Any {
        return when (agendaItemType) {
            AgendaItemType.REMINDER_ITEM -> {
                PendingReminderRetryEntity(
                    id = eventId,
                    action = agendaAction
                )
            }

            AgendaItemType.TASK_ITEM -> {
                PendingTaskRetryEntity(
                    id = eventId,
                    action = agendaAction
                )
            }

            AgendaItemType.EVENT_ITEM -> {}
        }
    }

    open fun toReminderEntity(): ReminderEntity {
        return ReminderEntity(
            id = eventId,
            description = description,
            title = title,
            remindAt = alarmType,
            time = timeInMillis,
            action = agendaAction,
            eventType = eventType
        )
    }

    fun toTaskEntity(isDone: Boolean): TaskEntity {
        return TaskEntity(
            id = eventId,
            description = description,
            title = title,
            remindAt = alarmType,
            time = timeInMillis,
            action = agendaAction,
            isDone = isDone,
            eventType = eventType
        )
    }
}
