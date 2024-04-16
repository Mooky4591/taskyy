package com.example.taskyy.data.local.room_entity.agenda_entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskyy.ui.enums.AgendaItemAction
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.objects.Task

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val description: String,
    val isDone: Boolean,
    val remindAt: Long,
    val time: Long,
    val title: String,
    val action: AgendaItemAction
) {
    fun transformToTask(): Task {
        return Task(
            title = title,
            description = description,
            isDone = isDone,
            alarmType = remindAt,
            timeInMillis = time,
            id = id,
            agendaItem = AgendaItemType.TASK_ITEM,
            agendaAction = action
        )
    }
}