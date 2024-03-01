package com.example.taskyy.domain.repository

import com.example.taskyy.data.local.room_entity.UserEntity

interface AuthRepository {
    fun addUserToDatabase(userEntity: UserEntity)
    fun registerUser(userEntity: UserEntity)
}