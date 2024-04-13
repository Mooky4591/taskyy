package com.example.taskyy.domain.repository

import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.objects.User

interface AuthRepository {
    suspend fun addTokenAndIdToDatabase(token: String, userId: String, email: String)
    suspend fun registerUser(user: User): Result<User, DataError.Network>
    fun validatePassword(password: String): Boolean
    suspend fun login(login: Login): Result<User, DataError.Network>
    suspend fun validateToken(): Boolean
}