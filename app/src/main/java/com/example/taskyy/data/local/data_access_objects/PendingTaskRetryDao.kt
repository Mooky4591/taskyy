package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.example.taskyy.data.local.room_entity.agenda_entities.PendingTaskRetryEntity

@Dao
interface PendingTaskRetryDao {
    @Upsert
    suspend fun insertPendingTask(taskRetryEntity: PendingTaskRetryEntity)

    @Delete
    suspend fun removePendingTask(taskRetryEntity: PendingTaskRetryEntity)

}