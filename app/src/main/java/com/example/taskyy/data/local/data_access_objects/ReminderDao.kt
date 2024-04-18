package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity

@Dao
interface ReminderDao {

    @Upsert
    suspend fun insertReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM reminder_table WHERE time BETWEEN :startTime AND :endTime")
    suspend fun getReminders(startTime: Long, endTime: Long): List<ReminderEntity>

    @Query("SELECT * FROM reminder_table WHERE id = :eventID")
    suspend fun getReminderByEventId(eventID: String): ReminderEntity

    @Delete
    suspend fun deleteReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM reminder_table WHERE time > :currentTime")
    suspend fun findEntitiesAfterCurrentTime(currentTime: Long): List<ReminderEntity>

}