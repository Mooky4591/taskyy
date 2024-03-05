package com.example.taskyy.domain.usecases

import android.util.Patterns

class LoginUseCase {

     fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}