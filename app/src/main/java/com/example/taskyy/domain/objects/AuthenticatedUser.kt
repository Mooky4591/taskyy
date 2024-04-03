package com.example.taskyy.domain.objects

data class AuthenticatedUser(
    var fullName: String?,
    var email: String?,
    var userId: String?,
    var token: String?
)
