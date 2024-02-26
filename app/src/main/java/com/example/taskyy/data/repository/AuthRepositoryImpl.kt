package com.example.taskyy.data.repository

import android.util.Patterns
import com.example.taskyy.domain.repository.AuthRepository

class AuthRepositoryImpl(

): AuthRepository {
    override fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //this is a place holder, I may not need this. Need to determine how to validate the name
    override fun isNameValid(name: String): Boolean {
        return true
    }
}