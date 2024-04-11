package com.example.taskyy.domain.repository

import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.error.Result
import com.example.taskyy.ui.objects.Reminder

interface AgendaRepository {
    suspend fun logout(): Boolean
    suspend fun getUserName(email: String): String
    suspend fun saveReminderToDB(reminder: Reminder): Result<Reminder, DataError.Local>
    suspend fun uploadReminderToApi(reminder: Reminder): Result<Reminder, DataError.Network>
    suspend fun getReminders(
        startDate: Long,
        endDate: Long
    ): Result<List<Reminder>, DataError.Local>

    suspend fun getReminderByEventId(eventId: String): Result<Reminder, DataError.Local>
}