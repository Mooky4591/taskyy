package com.example.taskyy.domain.repository

import com.example.taskyy.data.data_access_objects.UserDao
import com.example.taskyy.domain.User

interface AuthRepository {
    fun registerUser(user: User, userDao: UserDao)
}