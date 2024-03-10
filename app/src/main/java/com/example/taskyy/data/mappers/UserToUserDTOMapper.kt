package com.example.taskyy.data.mappers

import com.example.taskyy.data.remote.data_transfer_objects.RegisterUserDTO
import com.example.taskyy.domain.objects.User
import javax.inject.Inject

class UserToUserDTOMapper @Inject constructor(
) {

    fun mapUserToUserDTO(user: User): RegisterUserDTO {
        return RegisterUserDTO(fullName = user.fullName, email = user.email, password = user.password)
    }
}