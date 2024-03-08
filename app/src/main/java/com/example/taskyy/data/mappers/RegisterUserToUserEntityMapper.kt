package com.example.taskyy.data.mappers

import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.domain.data_transfer_objects.RegisterUserDTO
import javax.inject.Inject

class RegisterUserToUserEntityMapper @Inject constructor(
) {

    fun mapRegisterUserToUserEntity(registerUserDTO: RegisterUserDTO): UserEntity {
        return UserEntity(name = registerUserDTO.fullName, email = registerUserDTO.email)
    }
}