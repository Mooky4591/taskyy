package com.example.taskyy.data.repositories

import android.util.Log
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.data.remote.LoginUserResponse
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.data_transfer_objects.RegisterUserDTO
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.objects.User
import com.example.taskyy.domain.repository.AuthRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import retrofit2.awaitResponse
import javax.inject.Inject
import kotlin.math.log

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val retrofit: TaskyyApi,
): AuthRepository {

    override fun addUserToDatabase(user: User) {
        runBlocking {
            launch {
                userDao.insertUser(user.mapToUserEntity())
            }
        }
    }

    override fun addTokenAndIdToDatabase(response: LoginUserResponse, email: String) {
        runBlocking {
            launch {
                userDao.update(response.token, response.userId, email)
            }
        }
    }

    override suspend fun registerUser(user: User) {
        val userDto = user.mapToUserDTO()
        retrofit.registerUser(userDto)
    }

    private fun User.mapToUserEntity(): UserEntity {
        return UserEntity(null, fullName, email, null)
    }

    private fun User.mapToUserDTO(): RegisterUserDTO {
        return RegisterUserDTO(fullName, email, password)
    }

    override fun validatePassword(password: String): Boolean {
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasValidLength = password.length in 8..20

        return hasLowerCase && hasUpperCase && hasDigit && hasValidLength
    }

    override fun login(login: Login): Result<LoginUserResponse> {
        val loginUser = retrofit.loginUser(login)
        var returnStatement: Result<LoginUserResponse>
        loginUser.enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(
                call: Call<LoginUserResponse>,
                response: Response<LoginUserResponse>
            ) {
                val responseInfo: LoginUserResponse = response.body() as LoginUserResponse
                addTokenAndIdToDatabase(responseInfo, login.email)
                if(response.isSuccessful){
                    returnStatement = Result.success()
                } else {
                    returnStatement = Result.failure(exception = Throwable())
                }
            }

            override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
            }
        })
        return returnStatement
    }
}