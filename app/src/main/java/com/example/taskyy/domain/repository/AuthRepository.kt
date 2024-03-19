package com.example.taskyy.domain.repository

import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.objects.User

interface AuthRepository {
    suspend fun addTokenAndIdToDatabase(token: String, userId: String, email: String)
    suspend fun registerUser(user: User): Boolean
    fun validatePassword(password: String): Boolean
    suspend fun login(login: Login): Result<User>
}