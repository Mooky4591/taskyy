package com.example.taskyy.domain.repository

import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.error.Result
import com.example.taskyy.ui.objects.AgendaEventItem
import com.example.taskyy.ui.objects.Reminder
import com.example.taskyy.ui.objects.Task

interface AgendaRepository {
    suspend fun logout(): Boolean
    suspend fun getUserName(email: String): String

    //Reminder
    suspend fun saveReminderToDB(reminder: Reminder): Result<Reminder, DataError.Local>
    suspend fun updateReminderToApi(reminder: Reminder): Result<Reminder, DataError.Network>
    suspend fun getReminders(
        startDate: Long,
        endDate: Long
    ): Result<List<Reminder>, DataError.Local>

    suspend fun getReminderByEventId(eventId: String): Result<Reminder, DataError.Local>
    suspend fun deleteReminderInDb(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Local>
    suspend fun deleteReminderOnApi(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Network>

    //Failed Reminder API Call
    suspend fun addFailedReminderToRetry(reminder: Reminder): Result<Reminder, DataError.Local>

    //Tasks
    suspend fun saveTaskToDB(task: Task): Result<Task, DataError.Local>
    suspend fun updateTaskToApi(task: Task): Result<Task, DataError.Network>
    suspend fun getTasks(
        startDate: Long,
        endDate: Long
    ): Result<List<Task>, DataError.Local>

    suspend fun getTaskByEventId(eventId: String): Result<Task, DataError.Local>
    suspend fun deleteTaskInDb(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Local>
    suspend fun deleteTaskOnApi(agendaEventItem: AgendaEventItem): Result<Boolean, DataError.Network>

    //Failed Task Api Call
    suspend fun addFailedTaskToRetry(task: Task): Result<Task, DataError.Local>

    //WorkManager
    suspend fun startWorkManager()
    suspend fun getAllAgendaItemsWithFutureNotifications(): Result<List<AgendaEventItem>, DataError.Local>
    suspend fun updateReminderToDB(reminder: Reminder): Result<Reminder, DataError.Local>
}