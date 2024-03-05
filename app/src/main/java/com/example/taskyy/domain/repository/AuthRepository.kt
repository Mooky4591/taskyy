package com.example.taskyy.domain.repository

import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.domain.data_transfer_objects.RegisterUserDTO

interface AuthRepository {
    fun addUserToDatabase(userEntity: UserEntity)
    fun registerUser(registerUserDTO: RegisterUserDTO)
}