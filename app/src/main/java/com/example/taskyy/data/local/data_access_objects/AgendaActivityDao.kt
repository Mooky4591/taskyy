package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Upsert
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity

@Dao
interface AgendaActivityDao {

    @Upsert
    suspend fun insertReminder(reminderEntity: ReminderEntity)

    @androidx.room.Query("SELECT * FROM reminder_table WHERE userId = :userId and time = :time")
    suspend fun getReminders(userId: String, time: Long): List<ReminderEntity>

}