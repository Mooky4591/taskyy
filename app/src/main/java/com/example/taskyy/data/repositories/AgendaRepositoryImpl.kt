package com.example.taskyy.data.repositories

import android.util.Log
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.domain.repository.AgendaRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    private val retrofit: TaskyyApi,
    private val userDao: UserDao
    ): AgendaRepository {

    override suspend fun logout(): Boolean {
        return try {
            retrofit.logoutUser()
            true
        } catch (e: HttpException) {
            Log.e("Tag", e.code().toString())
            false
        } catch (i: IOException) {
            Log.e("Tag", i.message.toString())
            false
        }
    }

    override suspend fun getUserName(email: String): String {
        return userDao.getUserNameByEmail(email)
    }

}