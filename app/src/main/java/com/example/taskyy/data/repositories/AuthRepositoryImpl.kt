package com.example.taskyy.data.repositories

import android.util.Log
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.domain.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val retrofit: TaskyyApi
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
                Log.e("TAG", retrofit.toString())
                retrofit.registerUser()
            } catch (e: HttpException){
                Log.e("TAG", "HttpException, unexpected response")
            }
            Log.e("TAG", response.toString())
        } }
    }



}