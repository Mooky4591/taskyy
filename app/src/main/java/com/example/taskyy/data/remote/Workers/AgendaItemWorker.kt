package com.example.taskyy.data.remote.Workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskyy.data.local.room_database.TaskyyDatabase
import com.example.taskyy.data.local.room_entity.agenda_entities.PendingReminderRetryEntity
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.data_transfer_objects.ReminderDTO
import com.example.taskyy.domain.error.Result
import com.example.taskyy.ui.enums.AgendaItemAction
import retrofit2.HttpException

class AgendaItemWorker(
    private val appContext: Context,
    private val params: WorkerParameters,
    private val taskyyDatabase: TaskyyDatabase,
    private val taskyyApi: TaskyyApi
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return checkReminderRetryTable()
        //checkTaskRetryTable()
        //checkEventRetryTable()
    }

    private fun checkEventRetryTable() {
    }

    private fun checkTaskRetryTable() {
    }

    private suspend fun checkReminderRetryTable(): Result {
        val reminderList = taskyyDatabase.pendingReminderRetryDao().getPendingReminderRetries()
        if (reminderList.isNotEmpty()) {
            for (reminder in reminderList) {
                when (reminder.action) {
                    AgendaItemAction.CREATE -> {
                        try {
                            taskyyApi.createReminder(reminder.toReminderDTO())
                        } catch (e: HttpException) {
                            return Result.retry()
                        }
                    }

                    AgendaItemAction.UPDATE -> {
                        try {
                            taskyyApi.updateReminder(reminder.toReminderDTO())
                        } catch (e: HttpException) {
                            return Result.retry()
                        }
                    }

                    AgendaItemAction.DELETE -> {
                        try {
                            taskyyApi.deleteReminder(reminder.id)
                        } catch (e: HttpException) {
                            return Result.retry()
                        }
                    }
                }
            }
        }
        return Result.success()
    }
}

private fun PendingReminderRetryEntity.toReminderDTO(): ReminderDTO {
    return ReminderDTO(
        id,
        description,
        title,
        time,
        remindAt
    )
}
