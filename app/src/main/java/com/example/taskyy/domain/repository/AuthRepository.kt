package com.example.taskyy.domain.repository

interface AuthRepository {

    fun isEmailValid(email: String): Boolean
    fun isNameValid(name: String): Boolean
}