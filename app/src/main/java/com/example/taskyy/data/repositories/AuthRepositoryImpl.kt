package com.example.taskyy.data.repositories

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
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val retrofit: TaskyyApi,
): AuthRepository {

    override fun addUserToDatabase(user: User) {
        runBlocking { launch {
            userDao.insertUser(user.mapToUserEntity())
             }
        }
    }

    override fun addTokenAndIdToDatabase(response: LoginUserResponse, email: String) {
        runBlocking { launch {
            userDao.update(response.token, response.userId, email)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun registerUser(user: User) {
        val userDto = user.mapToUserDTO()
        GlobalScope.launch(Dispatchers.IO) {
            var response = retrofit.registerUser(userDto)
        }
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

    override fun login(login: Login): Call<LoginUserResponse> {
        return retrofit.loginUser(login)
    }
}