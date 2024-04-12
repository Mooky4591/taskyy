package com.example.taskyy.data.local.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskyy.data.local.data_access_objects.AgendaActivityDao
import com.example.taskyy.data.local.data_access_objects.PendingEventRetryDao
import com.example.taskyy.data.local.data_access_objects.PendingReminderRetryDao
import com.example.taskyy.data.local.data_access_objects.PendingTaskRetryDao
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.agenda_entities.EventEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.PendingEventRetryEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.PendingReminderRetryEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.PendingTaskRetryEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.ReminderEntity
import com.example.taskyy.data.local.room_entity.agenda_entities.TaskEntity
import com.example.taskyy.data.local.room_entity.user.UserEntity

@Database(
    entities = [UserEntity::class, ReminderEntity::class, TaskEntity::class, EventEntity::class, PendingReminderRetryEntity::class, PendingTaskRetryEntity::class, PendingEventRetryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskyyDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun agendaDao(): AgendaActivityDao
    abstract fun pendingReminderRetryDao(): PendingReminderRetryDao
    abstract fun pendingEventRetryDao(): PendingEventRetryDao
    abstract fun pendingTaskRetryDao(): PendingTaskRetryDao

    companion object {

        @Volatile
        private var INSTANCE: TaskyyDatabase? = null
        fun getDatabase(context: Context): TaskyyDatabase {
            if(INSTANCE != null) {
                return INSTANCE as TaskyyDatabase
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskyyDatabase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}