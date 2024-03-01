package com.example.taskyy.data.repositories

import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.data.remote.RetrofitInstance
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.domain.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val taskyyApi: TaskyyApi
): AuthRepository {

    override fun addUserToDatabase(userEntity: UserEntity) {
        runBlocking { launch {
            userDao.insertUser(userEntity)
             }
        }
    }

    override fun registerUser(userEntity: UserEntity) {
        runBlocking { launch {
            val response = try {
                //how to use the provideOkHttp and provideAuthApi here?
                taskyyApi.registerUser()
            } catch (E: Exception){}
        } }
    }



}