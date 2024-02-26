package com.example.taskyy.domain

import android.util.Patterns

class LoginUseCase {

     fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}