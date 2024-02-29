package com.example.taskyy.domain.repository

import com.example.taskyy.data.data_access_objects.UserDao
import com.example.taskyy.data.room_entity.UserEntity

interface AuthRepository {
    fun registerUser(userEntity: UserEntity, userDao: UserDao)
}