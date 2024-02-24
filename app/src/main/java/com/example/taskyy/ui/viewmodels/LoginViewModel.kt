package com.example.taskyy.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskyy.domain.repository.AuthRepository
import com.example.taskyy.ui.events.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.OnEmailChanged -> state = state.copy(email = event.email, isEmailValid = authRepository.isEmailValid(event.email))
            is LoginEvent.OnLoginClick -> login()
            is LoginEvent.OnPasswordChanged -> state = state.copy(password = event.password)
            is LoginEvent.OnTogglePasswordVisibility -> state = state.copy(isPasswordVisible = event.isPasswordVisible)
            is LoginEvent.OnSignUpClick -> register()
        }
    }

    private fun register() {
        TODO("Not yet implemented")
    }

    private fun login() {
        TODO("Not yet implemented")
    }

}

data class LoginState(
    var email: String = "",
    val password: String = "",
    val isLogginIn: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordVisible: Boolean = false
) {}

