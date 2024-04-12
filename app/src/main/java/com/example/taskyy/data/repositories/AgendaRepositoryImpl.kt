package com.example.taskyy.data.repositories

import android.util.Log
import com.example.taskyy.data.local.data_access_objects.AgendaActivityDao
import com.example.taskyy.data.local.data_access_objects.PendingReminderRetryDao
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.agenda_entities.PendingReminderRetryEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.data_transfer_objects.ReminderDTO
import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.domain.repository.UserPreferences
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.objects.AgendaEventItem
import com.example.taskyy.ui.objects.Reminder
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    private val retrofit: TaskyyApi,
    private val agendaDao: AgendaActivityDao,
    private val pendingReminderRetryDao: PendingReminderRetryDao,
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
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
            when (e.message) {
                "Permission denied" -> Result.Error(DataError.Local.PERMISSION_DENIED)
                "File not found" -> Result.Error(DataError.Local.FILE_NOT_FOUND)
                "Disk full" -> Result.Error(DataError.Local.DISK_FULL)
                "Input/output error" -> Result.Error(DataError.Local.INPUT_OUTPUT_ERROR)
                "Connection refused" -> Result.Error(DataError.Local.CONNECTION_REFUSED)
                else -> Result.Error(DataError.Local.UNKNOWN)
            }
        }
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

    override suspend fun addFailedReminderToRetry(reminder: Reminder): Result<Reminder, DataError.Local> {
        return try {
            pendingReminderRetryDao.insertPendingReminder(reminder.toPendingReminderRetryEntity())
            Result.Success(reminder)
        } catch (e: IOException) {
            when (e.message) {
                "Permission denied" -> Result.Error(DataError.Local.PERMISSION_DENIED)
                "File not found" -> Result.Error(DataError.Local.FILE_NOT_FOUND)
                "Disk full" -> Result.Error(DataError.Local.DISK_FULL)
                "Input/output error" -> Result.Error(DataError.Local.INPUT_OUTPUT_ERROR)
                "Connection refused" -> Result.Error(DataError.Local.CONNECTION_REFUSED)
                else -> Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }

    override suspend fun addFailedTaskToRetry() {
        TODO("Not yet implemented")
    }

    override suspend fun addFailedEventToRetry() {
        TODO("Not yet implemented")
    }

    override suspend fun getReminders(
        startDate: Long,
        endDate: Long
    ): Result<List<Reminder>, DataError.Local> {
        return try {
            val list = agendaDao.getReminders(startTime = startDate, endTime = endDate)
            Result.Success(list.transformToReminder())
        } catch (e: IOException) {
            when (e.message) {
                "Permission denied" -> Result.Error(DataError.Local.PERMISSION_DENIED)
                "File not found" -> Result.Error(DataError.Local.FILE_NOT_FOUND)
                "Disk full" -> Result.Error(DataError.Local.DISK_FULL)
                "Input/output error" -> Result.Error(DataError.Local.INPUT_OUTPUT_ERROR)
                "Connection refused" -> Result.Error(DataError.Local.CONNECTION_REFUSED)
                else -> Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }

    override suspend fun getReminderByEventId(eventId: String): Result<Reminder, DataError.Local> {
        return try {
            val reminderEntity = agendaDao.getReminderByEventId(eventId)
            Result.Success(reminderEntity.transformToReminder())
        } catch (e: IOException) {
            when (e.message) {
                "Permission denied" -> Result.Error(DataError.Local.PERMISSION_DENIED)
                "File not found" -> Result.Error(DataError.Local.FILE_NOT_FOUND)
                "Disk full" -> Result.Error(DataError.Local.DISK_FULL)
                "Input/output error" -> Result.Error(DataError.Local.INPUT_OUTPUT_ERROR)
                "Connection refused" -> Result.Error(DataError.Local.CONNECTION_REFUSED)
                else -> Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }

    override suspend fun deleteReminderInDb(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Local> {
        return try {
            agendaDao.deleteReminder(reminderEntity = agendaEventItem.toReminderEntity())
            Result.Success(true)
        } catch (e: IOException) {
            when (e.message) {
                "Permission denied" -> Result.Error(DataError.Local.PERMISSION_DENIED)
                "File not found" -> Result.Error(DataError.Local.FILE_NOT_FOUND)
                "Disk full" -> Result.Error(DataError.Local.DISK_FULL)
                "Input/output error" -> Result.Error(DataError.Local.INPUT_OUTPUT_ERROR)
                "Connection refused" -> Result.Error(DataError.Local.CONNECTION_REFUSED)
                else -> Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }
}

fun ReminderEntity.transformToReminder(): Reminder {
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

fun List<ReminderEntity>.transformToReminder(): List<Reminder> {
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

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = eventId,
        description = description,
        title = title,
        remindAt = alarmType,
        time = timeInMillis,
        action = agendaAction
    )
}

fun Reminder.toReminderDto(): ReminderDTO {
    return ReminderDTO(
        id = eventId,
        description = description,
        title = title,
        time = timeInMillis,
        remindAt = alarmType
    )
}

fun Reminder.toPendingReminderRetryEntity(): PendingReminderRetryEntity {
    return PendingReminderRetryEntity(
        id = eventId,
        description = title,
        action = agendaAction,
        remindAt = alarmType,
        title = title,
        time = timeInMillis,
    )
}

private fun AgendaEventItem.toPendingReminderRetryEntity(): PendingReminderRetryEntity {
    return PendingReminderRetryEntity(
        id = eventId,
        description = title,
        action = agendaAction,
        remindAt = alarmType,
        title = title,
        time = timeInMillis,
    )
}