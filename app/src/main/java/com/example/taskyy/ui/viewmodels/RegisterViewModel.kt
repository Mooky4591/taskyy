package com.example.taskyy.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskyy.data.local.data_access_objects.UserDao
import com.example.taskyy.data.local.room_entity.UserEntity
import com.example.taskyy.domain.repository.AuthRepository
import com.example.taskyy.ui.events.RegisterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDao: UserDao
):ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    fun onEvent(event: RegisterEvent){
        when(event) {
            is RegisterEvent.OnNameChanged -> state = state.copy(name = event.name)
            is RegisterEvent.OnEmailChanged -> state = state.copy(email = event.email)
            is RegisterEvent.OnPasswordChanged -> state = state.copy(password = event.password)
            is RegisterEvent.OnGetStartedClick -> register()
            is RegisterEvent.OnTogglePasswordVisibility -> state = state.copy(isPasswordVisible = event.isPasswordVisible)
        }
    }

    private fun register() {
        val userEntity = UserEntity(name = state.name, password = state.password, email = state.email)
        authRepository.addUserToDatabase(userEntity, userDao)
        authRepository.registerUser(userEntity)
    }
}

data class RegisterState(
        var email: String = "",
        var password: String = "",
        var name: String = "",
        var isEmailValid: Boolean = false,
        var isPasswordVisible: Boolean = false,
    ) {}

