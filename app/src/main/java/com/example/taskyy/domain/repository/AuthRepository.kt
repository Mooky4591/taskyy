package com.example.taskyy.domain.repository

import com.example.taskyy.data.remote.LoginUserResponse
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.objects.User
import retrofit2.Call

interface AuthRepository {
    fun addUserToDatabase(user: User)
    fun addTokenAndIdToDatabase(response: LoginUserResponse, email: String)
    fun registerUser(user: User)
    fun validatePassword(password: String): Boolean
    fun login(login: Login): Call<LoginUserResponse>
}