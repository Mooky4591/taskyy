package com.example.taskyy.data.repository

import com.example.taskyy.data.data_access_objects.UserDao
import com.example.taskyy.domain.User
import com.example.taskyy.domain.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthRepositoryImpl(): AuthRepository {
    override fun registerUser(user: User, userDao: UserDao) {
        runBlocking { launch {
            userDao.insertUser(user)
             }
        }
    }
}