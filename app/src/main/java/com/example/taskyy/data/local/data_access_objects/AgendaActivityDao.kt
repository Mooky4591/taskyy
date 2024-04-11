package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Upsert
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity

@Dao
interface AgendaActivityDao {

    @Upsert
    suspend fun insertReminder(reminderEntity: ReminderEntity)

    @androidx.room.Query("SELECT * FROM reminder_table WHERE time BETWEEN :startTime AND :endTime")
    suspend fun getReminders(startTime: Long, endTime: Long): List<ReminderEntity>

    @androidx.room.Query("SELECT * FROM reminder_table WHERE id = :eventID")
    suspend fun getReminderByEventId(eventID: String): ReminderEntity

}