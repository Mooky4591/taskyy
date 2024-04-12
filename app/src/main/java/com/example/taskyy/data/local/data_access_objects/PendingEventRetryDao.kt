package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.example.taskyy.data.local.room_entity.agenda_entities.PendingEventRetryEntity

@Dao
interface PendingEventRetryDao {
    @Upsert
    suspend fun insertPendingEvent(eventRetryEntity: PendingEventRetryEntity)

    @Delete
    suspend fun removePendingEvent(eventRetryEntity: PendingEventRetryEntity)

}