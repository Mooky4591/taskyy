package com.example.taskyy.domain.usecases

import android.util.Patterns
import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.objects.User
import com.example.taskyy.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    suspend fun loginUser(login: Login): Result<User, DataError.Network> {
        return authRepository.login(login)
    }

}