package com.example.taskyy.domain.repository

import com.example.taskyy.domain.error.DataError
import com.example.taskyy.ui.objects.Reminder

interface AgendaRepository {
    suspend fun logout(): Boolean
    suspend fun getUserName(email: String): String
    suspend fun saveReminder(reminder: Reminder): com.example.taskyy.domain.error.Result<Reminder, DataError.Network>
}