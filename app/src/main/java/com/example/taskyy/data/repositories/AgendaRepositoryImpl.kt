package com.example.taskyy.data.repositories

import android.util.Log
import com.example.taskyy.data.local.data_access_objects.AgendaActivityDao
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.ReminderEntity
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.data_transfer_objects.ReminderDTO
import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.ui.objects.Reminder
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    private val retrofit: TaskyyApi,
    private val agendaDao: AgendaActivityDao,
    private val userDao: UserDao
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

    override suspend fun saveReminder(reminder: Reminder): com.example.taskyy.domain.error.Result<Reminder, DataError.Network> {
        return try {
            val reminderEntity = reminder.toReminderEntity()
            val reminderDTO = reminder.toReminderDto()
            retrofit.createReminder(reminderDTO)
            agendaDao.insertReminder(reminderEntity)
            com.example.taskyy.domain.error.Result.Success(reminder)
        } catch (e: HttpException) {
            when (e.code()) {
                408 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.REQUEST_TIMEOUT)
                429 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.SERVER_ERROR)
                400 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.SERIALIZATION)
                else -> com.example.taskyy.domain.error.Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

}

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        description = description,
        title = title,
        remindAt = alarmType,
        time = timeInMillis
    )
}

fun Reminder.toReminderDto(): ReminderDTO {
    return ReminderDTO(
        id = id,
        description = description,
        title = title,
        time = timeInMillis,
        remindAt = alarmType
    )
}