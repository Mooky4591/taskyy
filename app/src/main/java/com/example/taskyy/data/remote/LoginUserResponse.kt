package com.example.taskyy.data.remote

data class LoginUserResponse(
    val fullName: String,
    val token: String,
    val userId: String
)