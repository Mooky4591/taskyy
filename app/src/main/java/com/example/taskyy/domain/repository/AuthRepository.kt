package com.example.taskyy.domain.repository

import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.domain.objects.RegisterUser

interface AuthRepository {
    fun addUserToDatabase(userEntity: UserEntity)
    fun registerUser(registerUser: RegisterUser)
}