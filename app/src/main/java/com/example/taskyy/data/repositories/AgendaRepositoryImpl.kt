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
import com.example.taskyy.data.local.data_access_objects.PendingReminderRetryDao
import com.example.taskyy.data.local.data_access_objects.PendingTaskRetryDao
import com.example.taskyy.data.local.data_access_objects.ReminderDao
import com.example.taskyy.data.local.data_access_objects.TaskDao
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.TaskEntity
import com.example.taskyy.data.local.room_entity.pending_agenda_retry.PendingReminderRetryEntity
import com.example.taskyy.data.local.room_entity.pending_agenda_retry.PendingTaskRetryEntity
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.workers.AgendaItemWorker
import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.objects.AgendaEventItem
import com.example.taskyy.ui.objects.Reminder
import com.example.taskyy.ui.objects.Task
import retrofit2.HttpException
import java.io.IOException
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    private val retrofit: TaskyyApi,
    private val reminderDao: ReminderDao,
    private val taskDao: TaskDao,
    private val pendingReminderRetryDao: PendingReminderRetryDao,
    private val pendingTaskRetryDao: PendingTaskRetryDao,
    private val userDao: UserDao,
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

    //Reminder Functions
    override suspend fun saveReminderToDB(reminder: Reminder): Result<Reminder, DataError.Local> {
        val reminderEntity = reminder.toReminderEntity()
        return try {
            reminderDao.insertReminder(reminderEntity)
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
            pendingReminderRetryDao.insertPendingReminder(
                agendaEventItem.toPendingRetryEntity(
                    agendaEventItem.eventType
                ) as PendingReminderRetryEntity
            )
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

    override suspend fun getReminders(
        startDate: Long,
        endDate: Long
    ): Result<MutableList<Reminder>, DataError.Local> {
        return try {
            val list = reminderDao.getReminders(startTime = startDate, endTime = endDate)
            Result.Success(list.transformToReminderList())
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
            val reminderEntity = reminderDao.getReminderByEventId(eventId)
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
            reminderDao.deleteReminder(reminderEntity = agendaEventItem.toReminderEntity())
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

    //Failed Reminder API calls
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

    //Failed Task API Calls
    override suspend fun addFailedTaskToRetry(task: Task): Result<Task, DataError.Local> {
        return try {
            pendingTaskRetryDao.insertPendingTask(task.toPendingTaskRetryEntity())
            Result.Success(task)
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

    //Task Functions
    override suspend fun saveTaskToDB(task: Task): Result<Task, DataError.Local> {
        val taskEntity = task.toTaskEntity()
        return try {
            taskDao.insertTask(taskEntity)
            Result.Success(task)
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

    override suspend fun saveTaskToApi(task: Task): Result<Task, DataError.Network> {
        val taskDTO = task.toTaskDTO()
        return try {
            retrofit.createTask(taskDTO)
            Result.Success(task)
        } catch (e: HttpException) {
            pendingTaskRetryDao.insertPendingTask(task.toPendingTaskRetryEntity())
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

    override suspend fun updateTaskToApi(task: Task): Result<Task, DataError.Network> {
        val taskDTO = task.toTaskDTO()
        return try {
            retrofit.updateTask(taskDTO)
            Result.Success(task)
        } catch (e: HttpException) {
            pendingTaskRetryDao.insertPendingTask(task.toPendingTaskRetryEntity())
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

    override suspend fun getTasks(
        startDate: Long,
        endDate: Long
    ): Result<MutableList<Task>, DataError.Local> {
        return try {
            val list = taskDao.getTasks(startTime = startDate, endTime = endDate)
            Result.Success(list.transformToTaskList())
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

    override suspend fun getTaskByEventId(eventId: String): Result<Task, DataError.Local> {
        return try {
            val taskEntity = taskDao.getTaskByEventId(eventId)
            Result.Success(taskEntity.transformToTask())
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

    override suspend fun deleteTaskInDb(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Local> {
        return try {
            taskDao.deleteTask(taskEntity = agendaEventItem.toTaskEntity(false))
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

    override suspend fun deleteTaskOnApi(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Network> {
        return try {
            retrofit.deleteTask(agendaEventItem.eventId)
            Result.Success(true)
        } catch (e: HttpException) {
            pendingTaskRetryDao.insertPendingTask(
                agendaEventItem.toPendingRetryEntity(
                    agendaEventItem.eventType
                ) as PendingTaskRetryEntity
            )
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

    //Work Manager
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
            workRequest,
        )
    }
}

//Extension Functions
private fun List<TaskEntity>.transformToTaskList(): MutableList<Task> {
    return map { taskEntity ->
        Task(
            title = taskEntity.title,
            description = taskEntity.description,
            alarmType = taskEntity.remindAt,
            timeInMillis = taskEntity.time,
            id = taskEntity.id,
            isDone = taskEntity.isDone,
            agendaAction = taskEntity.action,
            agendaItem = AgendaItemType.TASK_ITEM
        )
    }.toMutableList()
}

fun MutableList<ReminderEntity>.transformToReminderList(): MutableList<Reminder> {
    return map { reminderEntity ->
        Reminder(
            title = reminderEntity.title,
            description = reminderEntity.description,
            timeInMillis = reminderEntity.time,
            alarmType = reminderEntity.remindAt,
            id = reminderEntity.id,
            agendaItem = AgendaItemType.REMINDER_ITEM,
            agendaAction = reminderEntity.action
        )
    }.toMutableList()
}