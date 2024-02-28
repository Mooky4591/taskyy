package com.example.taskyy.data.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskyy.data.data_access_objects.UserDao
import com.example.taskyy.data.room_entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class TaskyyDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: RoomDatabase? = null
        fun getDatabase(context: Context): RoomDatabase{
            if(INSTANCE != null) {
                return INSTANCE as RoomDatabase
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}