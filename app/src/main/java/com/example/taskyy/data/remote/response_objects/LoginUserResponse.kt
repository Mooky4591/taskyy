package com.example.taskyy.data.remote.response_objects

data class LoginUserResponse(
    val fullName: String,
    val token: String,
    val userId: String
)