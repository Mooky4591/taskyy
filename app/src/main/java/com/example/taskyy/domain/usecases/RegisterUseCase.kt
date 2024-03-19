package com.example.taskyy.domain.usecases

import android.util.Patterns
import com.example.taskyy.domain.objects.User
import com.example.taskyy.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

     suspend fun registerUser(user: User): Boolean {
         return authRepository.registerUser(user)
     }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return authRepository.validatePassword(password)
    }
}