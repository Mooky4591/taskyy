package com.example.taskyy.data.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskyy.data.room_entity.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
}