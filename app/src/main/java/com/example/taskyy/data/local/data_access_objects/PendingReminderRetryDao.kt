package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.taskyy.data.local.room_entity.pending_agenda_retry.PendingReminderRetryEntity

@Dao
interface PendingReminderRetryDao {
    @Upsert
    suspend fun insertPendingReminder(reminderRetryEntity: PendingReminderRetryEntity)

    @Delete
    suspend fun removePendingReminder(reminderRetryEntity: PendingReminderRetryEntity)

    @Query("SELECT * FROM pending_event_reminder_table")
    suspend fun getPendingReminderRetries(): List<PendingReminderRetryEntity>

}