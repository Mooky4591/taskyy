package com.example.taskyy.data.repositories

import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.data_transfer_objects.RegisterUserDTO
import com.example.taskyy.data.remote.response_objects.LoginUserResponse
import com.example.taskyy.domain.error.DataError
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.objects.User
import com.example.taskyy.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val retrofit: TaskyyApi,
): AuthRepository {

    override suspend fun addTokenAndIdToDatabase(token: String, userId: String, email: String) {
        userDao.update(token, userId, email)
    }

    override suspend fun registerUser(user: User): com.example.taskyy.domain.error.Result<User, DataError.Network> {
        val userDto = user.toUserDTO()
        return try {
            retrofit.registerUser(userDto)
            com.example.taskyy.domain.error.Result.Success(user)
        } catch (e: HttpException) {
            when (e.code()) {
                408 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.REQUEST_TIMEOUT)
                429 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.SERVER_ERROR)
                400 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.SERIALIZATION)
                else -> com.example.taskyy.domain.error.Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override fun validatePassword(password: String): Boolean {
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasValidLength = password.length in 8..20

        return hasLowerCase && hasUpperCase && hasDigit && hasValidLength
    }

    override suspend fun login(login: Login): com.example.taskyy.domain.error.Result<User, DataError.Network> {
        return try {
            val loginUser = retrofit.loginUser(login)
            val user = loginUser.toUser()
            val userEntity = loginUser.toUserEntity(login.email, loginUser.userId)
            userDao.insertUser(userEntity)
            addTokenAndIdToDatabase(loginUser.token, loginUser.userId, login.email)
            com.example.taskyy.domain.error.Result.Success(user)
        } catch (e: HttpException) {
            when (e.code()) {
                408 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.REQUEST_TIMEOUT)
                429 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                413 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                500 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.SERVER_ERROR)
                400 -> com.example.taskyy.domain.error.Result.Error(DataError.Network.SERIALIZATION)
                else -> com.example.taskyy.domain.error.Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: IOException) {
            when (e.message) {
                "UnknownHostException" -> com.example.taskyy.domain.error.Result.Error(DataError.Network.UNKNOWN_HOST_EXCEPTION)
                else -> com.example.taskyy.domain.error.Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    private fun User.toUserDTO(): RegisterUserDTO {
        return RegisterUserDTO(fullName, email, password)
    }

    private fun LoginUserResponse.toUser(): User {
        return User(fullName, "", "")
    }

    private fun LoginUserResponse.toUserEntity(email: String, userId: String): UserEntity {
        return UserEntity(id = userId, name = fullName, email = email, token = null)
    }
}
