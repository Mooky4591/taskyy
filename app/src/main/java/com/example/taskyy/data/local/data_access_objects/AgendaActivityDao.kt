package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Upsert
import com.example.taskyy.data.local.room_entity.ReminderEntity

@Dao
interface AgendaActivityDao {

    @Upsert
    suspend fun insertReminder(reminderEntity: ReminderEntity)

}