package com.example.taskyy.data.repositories

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.taskyy.data.local.data_access_objects.AgendaActivityDao
import com.example.taskyy.data.local.data_access_objects.PendingReminderRetryDao
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.agenda_entities.PendingReminderRetryEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.Workers.AgendaItemWorker
import com.example.taskyy.data.remote.data_transfer_objects.ReminderDTO
import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.error.LocalDataErrorHelper
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.domain.repository.UserPreferences
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.objects.AgendaEventItem
import com.example.taskyy.ui.objects.Reminder
import retrofit2.HttpException
import java.io.IOException
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    private val retrofit: TaskyyApi,
    private val agendaDao: AgendaActivityDao,
    private val pendingReminderRetryDao: PendingReminderRetryDao,
    private val userDao: UserDao,
    private val userPreferences: UserPreferences,
    private val context: Context
): AgendaRepository {

    override suspend fun logout(): Boolean {
        return try {
            retrofit.logoutUser()
            true
        } catch (e: HttpException) {
            Log.e("Tag", e.code().toString())
            false
        } catch (i: IOException) {
            Log.e("Tag", i.message.toString())
            false
        }
    }

    override suspend fun getUserName(email: String): String {
        return userDao.getUserNameByEmail(email)
    }

    override suspend fun saveReminderToDB(reminder: Reminder): Result<Reminder, DataError.Local> {
        val reminderEntity = reminder.toReminderEntity()
        return try {
            agendaDao.insertReminder(reminderEntity)
            Result.Success(reminder)
        } catch (e: IOException) {
            (
                    LocalDataErrorHelper.determineLocalDataErrorMessage(e.message!!)
                    )
        } as Result<Reminder, DataError.Local>
    }

    override suspend fun saveReminderToApi(reminder: Reminder): Result<Reminder, DataError.Network> {
        val reminderDTO = reminder.toReminderDto()
        return try {
            retrofit.createReminder(reminderDTO)
            Result.Success(reminder)
        } catch (e: HttpException) {
            pendingReminderRetryDao.insertPendingReminder(reminder.toPendingReminderRetryEntity())
            when (e.code()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                400 -> Result.Error(DataError.Network.SERIALIZATION)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun updateReminderToApi(reminder: Reminder): Result<Reminder, DataError.Network> {
        val reminderDTO = reminder.toReminderDto()
        return try {
            retrofit.updateReminder(reminderDTO)
            Result.Success(reminder)
        } catch (e: HttpException) {
            pendingReminderRetryDao.insertPendingReminder(reminder.toPendingReminderRetryEntity())
            when (e.code()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                400 -> Result.Error(DataError.Network.SERIALIZATION)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun deleteReminderOnApi(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Network> {
        return try {
            retrofit.deleteReminder(agendaEventItem.eventId)
            Result.Success(true)
        } catch (e: HttpException) {
            pendingReminderRetryDao.insertPendingReminder(agendaEventItem.toPendingReminderRetryEntity())
            when (e.code()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> Result.Error(DataError.Network.SERVER_ERROR)
                400 -> Result.Error(DataError.Network.SERIALIZATION)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun startWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<AgendaItemWorker>(
            repeatInterval = 2,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofSeconds(15)
        ).setConstraints(constraints)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            "AgendaItemWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
    override suspend fun getReminders(
        startDate: Long,
        endDate: Long
    ): Result<List<Reminder>, DataError.Local> {
        return try {
            val list = agendaDao.getReminders(startTime = startDate, endTime = endDate)
            Result.Success(list.transformToReminder())
        } catch (e: IOException) {
            (
                    LocalDataErrorHelper.determineLocalDataErrorMessage(e.message!!)
                    )
        } as Result<List<Reminder>, DataError.Local>
        }

    override suspend fun getReminderByEventId(eventId: String): Result<Reminder, DataError.Local> {
        return try {
            val reminderEntity = agendaDao.getReminderByEventId(eventId)
            Result.Success(reminderEntity.transformToReminder())
        } catch (e: IOException) {
            (
                    LocalDataErrorHelper.determineLocalDataErrorMessage(e.message!!)
                    )
        } as Result<Reminder, DataError.Local>
    }

    override suspend fun deleteReminderInDb(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Local> {
        return try {
            agendaDao.deleteReminder(reminderEntity = agendaEventItem.toReminderEntity())
            Result.Success(true)
        } catch (e: IOException) {
            (
                    LocalDataErrorHelper.determineLocalDataErrorMessage(e.message!!)
                    )
        } as Result<Boolean, DataError.Local>
    }
}

private fun ReminderEntity.transformToReminder(): Reminder {
    return Reminder(
        title,
        description,
        time,
        remindAt,
        id,
        AgendaItemType.REMINDER_ITEM,
        action
    )
}

private fun List<ReminderEntity>.transformToReminder(): List<Reminder> {
    return map { reminderEntity ->
        Reminder(
            reminderEntity.title,
            reminderEntity.description,
            reminderEntity.time,
            reminderEntity.remindAt,
            reminderEntity.id,
            AgendaItemType.REMINDER_ITEM,
            reminderEntity.action
        )
    }
}


private fun AgendaEventItem.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = eventId,
        description = description,
        title = title,
        remindAt = alarmType,
        time = timeInMillis,
        action = agendaAction
    )
}

private fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = eventId,
        description = description,
        title = title,
        remindAt = alarmType,
        time = timeInMillis,
        action = agendaAction
    )
}

private fun Reminder.toReminderDto(): ReminderDTO {
    return ReminderDTO(
        id = eventId,
        description = description,
        title = title,
        time = timeInMillis,
        remindAt = alarmType
    )
}

private fun Reminder.toPendingReminderRetryEntity(): PendingReminderRetryEntity {
    return PendingReminderRetryEntity(
        id = eventId,
        action = agendaAction,
    )
}

private fun AgendaEventItem.toPendingReminderRetryEntity(): PendingReminderRetryEntity {
    return PendingReminderRetryEntity(
        id = eventId,
        action = agendaAction,
    )
}