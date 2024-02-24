package com.example.taskyy.data.repository

import android.util.Patterns
import com.example.taskyy.domain.repository.AuthRepository

class AuthRepositoryImpl(

): AuthRepository {
    override fun isEmailValid(email: String): Boolean {
        //super.isEmailValid(email)
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }
}