package com.example.taskyy.domain.usecases

import com.example.taskyy.domain.objects.RegisterUser
import com.example.taskyy.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun registerUser(registerUser: RegisterUser) {
        authRepository.registerUser(registerUser)
    }
}