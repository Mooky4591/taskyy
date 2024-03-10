package com.example.taskyy.data.local.room_entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    var id: Int? = null,
    var name: String,
    var email: String,
    var token: String?
) {
}