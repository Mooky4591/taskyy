package com.example.taskyy.data.local.data_access_objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.taskyy.data.local.room_entity.agenda_entities.TaskEntity

@Dao
interface TaskDao {
    @Upsert
    suspend fun insertTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM task_table WHERE time BETWEEN :startTime AND :endTime")
    suspend fun getTasks(startTime: Long, endTime: Long): MutableList<TaskEntity>

    @Query("SELECT * FROM task_table WHERE id = :eventID")
    suspend fun getTaskByEventId(eventID: String): TaskEntity

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)

}