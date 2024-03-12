package com.example.taskyy.domain.usecases

import com.example.taskyy.domain.objects.User
import com.example.taskyy.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

     suspend fun registerUser(user: User) {
        authRepository.registerUser(user)
        authRepository.addUserToDatabase(user)
    }

    fun isPasswordValid(password: String): Boolean {
        return authRepository.validatePassword(password)
    }
}