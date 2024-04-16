package com.example.taskyy.data.remote.Workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskyy.data.local.room_database.TaskyyDatabase
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.data_transfer_objects.ReminderDTO
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
                            taskyyApi.createReminder(
                                taskyyDatabase.agendaDao().getReminderByEventId(reminder.id)
                                    .toReminderDTO()
                            )
                            taskyyDatabase.pendingReminderRetryDao().removePendingReminder(reminder)
                            return Result.success()
                        } catch (e: HttpException) {
                            return shouldTryAgainOrFail(e.code())
                        }
                    }

                    AgendaItemAction.UPDATE -> {
                        try {
                            taskyyApi.updateReminder(
                                taskyyDatabase.agendaDao().getReminderByEventId(reminder.id)
                                    .toReminderDTO()
                            )
                            taskyyDatabase.pendingReminderRetryDao().removePendingReminder(reminder)
                            return Result.success()
                        } catch (e: HttpException) {
                            return shouldTryAgainOrFail(e.code())
                        }
                    }

                    AgendaItemAction.DELETE -> {
                        try {
                            taskyyApi.deleteReminder(reminder.id)
                            taskyyDatabase.pendingReminderRetryDao().removePendingReminder(reminder)
                            return Result.success()
                        } catch (e: HttpException) {
                            return shouldTryAgainOrFail(e.code())
                        }
                    }
                }
            }
        }
        return Result.success()
    }

    private fun shouldTryAgainOrFail(code: Int): Result {
        return when (code) {
            in 500..599 -> Result.retry()
            429 -> Result.retry()
            in 400..410 -> Result.failure()
            else -> {
                Result.failure()
            }
        }

    }
}

private fun ReminderEntity.toReminderDTO(): ReminderDTO {
    return ReminderDTO(
        id = id,
        remindAt = remindAt,
        description = description,
        title = title,
        time = time
    )
}
