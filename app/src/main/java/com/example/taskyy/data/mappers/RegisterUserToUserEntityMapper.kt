package com.example.taskyy.data.mappers

import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.domain.objects.RegisterUser
import javax.inject.Inject

class RegisterUserToUserEntityMapper @Inject constructor(
) {

    fun mapRegisterUserToUserEntity(registerUser: RegisterUser): UserEntity {
        return UserEntity(name = registerUser.fullName, email = registerUser.email)
    }
}