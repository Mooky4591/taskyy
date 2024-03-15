package com.example.taskyy.data.repositories

import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.data.remote.response_objects.LoginUserResponse
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.data.remote.data_transfer_objects.RegisterUserDTO
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

    override suspend fun addUserToDatabase(user: User) {
        userDao.insertUser(user.mapToUserEntity())
    }

    override suspend fun addTokenAndIdToDatabase(token: String, userId: String, email: String) {
        userDao.update(token, userId, email)
    }

    override suspend fun registerUser(user: User): Boolean {
        val userDto = user.mapToUserDTO()
        var wasRegistrationSuccessful = true
        try {
            retrofit.registerUser(userDto)
        } catch (e: HttpException) {
            wasRegistrationSuccessful = false
        }
        return wasRegistrationSuccessful
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

    override suspend fun login(login: Login): Result<User> {
        return try {
            val user = retrofit.loginUser(login)
            addTokenAndIdToDatabase(user.token, user.userId, login.email)
            Result.success(user.toUser())
        } catch(e: HttpException) {
            Result.failure(e)
        } catch(e: IOException) {
            Result.failure(e)
        }
    }

    private fun LoginUserResponse.toUser(): User {
        return User(fullName, "", "")
    }

}
