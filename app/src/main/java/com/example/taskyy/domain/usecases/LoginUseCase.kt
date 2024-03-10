package com.example.taskyy.domain.usecases

import android.util.Patterns
import com.example.taskyy.data.remote.LoginUserResponse
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.repository.AuthRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

     fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun loginUser(login: Login): Boolean {
        val loginUser = authRepository.login(login)
        var wasLoginSuccessful = false
        loginUser.enqueue(object : Callback<LoginUserResponse>{
            override fun onResponse(
                call: Call<LoginUserResponse>,
                response: Response<LoginUserResponse>
            ) {
                val responseInfo: LoginUserResponse = response.body() as LoginUserResponse
                authRepository.addTokenAndIdToDatabase(responseInfo, login.email)
                if(response.isSuccessful) {
                    wasLoginSuccessful = true
                }
            }

            override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
            }

        })

        return wasLoginSuccessful
    }
}