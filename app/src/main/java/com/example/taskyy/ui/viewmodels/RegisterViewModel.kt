package com.example.taskyy.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskyy.domain.repository.AuthRepository
import com.example.taskyy.ui.events.RegisterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
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
        authRepository.registerUser(name = state.name, password = state.password, email = state.email)
    }
}

    data class RegisterState(
        var email: String = "",
        var password: String = "",
        var name: String = "",
        var isEmailValid: Boolean = false,
        var isPasswordVisible: Boolean = false
    ) {}

