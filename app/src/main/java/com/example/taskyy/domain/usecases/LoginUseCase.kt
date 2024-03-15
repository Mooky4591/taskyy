package com.example.taskyy.domain.usecases

import android.util.Patterns
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    suspend fun loginUser(login: Login): Boolean {
        val response = authRepository.login(login)
        return response.isSuccess
    }

    fun isPasswordValid(password: String): Boolean {
        return authRepository.validatePassword(password)
    }
}