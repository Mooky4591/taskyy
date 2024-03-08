package com.example.taskyy.domain.usecases

import com.example.taskyy.domain.objects.User
import com.example.taskyy.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun registerUser(user: User) {
        authRepository.registerUser(user)
    }
}