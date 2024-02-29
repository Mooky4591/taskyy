package com.example.taskyy.data.room_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id: Int,
    @ColumnInfo val firstName: String,
    @ColumnInfo val lastName: String,
    @ColumnInfo val phoneNumber: String
) {
}