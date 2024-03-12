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
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.await
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject
import kotlin.math.log

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val retrofit: TaskyyApi,
): AuthRepository {

    override suspend fun addUserToDatabase(user: User) {
        userDao.insertUser(user.mapToUserEntity())
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

    override suspend fun login(login: Login): Result<LoginUserResponse> {
        return try {
            val user = retrofit.loginUser(login)
            Result.success(user)
        } catch(e: HttpException) {
            Result.failure(e)
        } catch(e: IOException) {
            Result.failure(e)
        }
    }
}
