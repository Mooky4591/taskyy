package com.example.taskyy.data.repositories

import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.data.remote.RetrofitInstance
import com.example.taskyy.domain.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class AuthRepositoryImpl(): AuthRepository {
    override fun addUserToDatabase(userEntity: UserEntity, userDao: UserDao) {
        runBlocking { launch {
            userDao.insertUser(userEntity)
             }
        }
    }

    override fun registerUser(userEntity: UserEntity) {
        runBlocking { launch {
            val response = try {
                RetrofitInstance.api.registerUser()
            } catch (E: Exception){}
        } }
    }



}