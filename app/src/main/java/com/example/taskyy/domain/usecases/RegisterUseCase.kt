package com.example.taskyy.domain.usecases

import com.example.taskyy.domain.data_transfer_objects.RegisterUserDTO
import com.example.taskyy.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun registerUser(registerUserDTO: RegisterUserDTO) {
        authRepository.registerUser(registerUserDTO)
    }
}