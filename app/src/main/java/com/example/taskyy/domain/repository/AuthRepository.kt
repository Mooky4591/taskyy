package com.example.taskyy.domain.repository

import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.domain.objects.User

interface AuthRepository {
    fun addUserToDatabase(userEntity: UserEntity)
    fun registerUser(user: User)
    fun validatePassword(password: String): Boolean
}