package com.example.taskyy.domain.repository

import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity

interface AuthRepository {
    fun addUserToDatabase(userEntity: UserEntity, userDao: UserDao)
    fun registerUser(userEntity: UserEntity)
}