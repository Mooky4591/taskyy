package com.example.taskyy.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskyy.domain.LoginUseCase
import com.example.taskyy.domain.repository.AuthRepository
import com.example.taskyy.ui.events.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.OnEmailChanged -> state = state.copy(email = event.email, isEmailValid = loginUseCase.isEmailValid(event.email))
            is LoginEvent.OnLoginClick -> login()
            is LoginEvent.OnPasswordChanged -> state = state.copy(password = event.password)
            is LoginEvent.OnTogglePasswordVisibility -> state = state.copy(isPasswordVisible = event.isPasswordVisible)
            is LoginEvent.OnNameChanged -> state = state.copy(name = event.name)
        }
    }

    private fun login() {
        //make API call
    }

}

data class LoginState(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var isLogginIn: Boolean = false,
    var isEmailValid: Boolean = false,
    var isPasswordVisible: Boolean = false,
) {}

