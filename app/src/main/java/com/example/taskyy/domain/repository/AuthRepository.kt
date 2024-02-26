package com.example.taskyy.domain.repository

interface AuthRepository {
    fun registerUser(name: String, email: String, password: String)
}