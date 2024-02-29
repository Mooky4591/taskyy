package com.example.taskyy.data.repository

import com.example.taskyy.data.data_access_objects.UserDao
import com.example.taskyy.data.room_entity.UserEntity
import com.example.taskyy.domain.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthRepositoryImpl(): AuthRepository {
    override fun registerUser(userEntity: UserEntity, userDao: UserDao) {
        runBlocking { launch {
            userDao.insertUser(userEntity)
             }
        }
    }
}