package com.example.taskyy.domain.data_transfer_objects

data class RegisterUserDTO(
    val fullName: String,
    val email: String,
    val password: String
) {}