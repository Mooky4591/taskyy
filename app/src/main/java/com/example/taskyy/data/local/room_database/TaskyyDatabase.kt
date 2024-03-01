package com.example.taskyy.data.local.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class TaskyyDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

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