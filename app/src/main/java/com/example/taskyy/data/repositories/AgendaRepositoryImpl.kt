package com.example.taskyy.data.repositories

import android.util.Log
import com.example.taskyy.data.remote.TaskyyApi
import com.example.taskyy.domain.repository.AgendaRepository
import retrofit2.HttpException
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    private val retrofit: TaskyyApi,
    ): AgendaRepository {

    override suspend fun logout(): Boolean {
        var wasLogoutSuccessful = true
        try {
            retrofit.logoutUser()
        } catch (e: HttpException) {
            Log.e("Tag", e.code().toString())
            wasLogoutSuccessful = false
        }
        return wasLogoutSuccessful
    }

}