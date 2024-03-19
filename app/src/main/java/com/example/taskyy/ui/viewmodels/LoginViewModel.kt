package com.example.taskyy.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.usecases.LoginUseCase
import com.example.taskyy.ui.events.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> state = state.copy(
                email = event.email,
                isEmailValid = loginUseCase.isEmailValid(event.email)
            )

            is LoginEvent.OnLoginClick -> login(event.login)
            is LoginEvent.OnPasswordChanged -> state = state.copy(password = event.password)
            is LoginEvent.OnTogglePasswordVisibility -> state =
                state.copy(isPasswordVisible = event.isPasswordVisible)

            is LoginEvent.OnNameChanged -> state = state.copy(name = event.name)
            is LoginEvent.OnRegisterLinkClick -> {}
            is LoginEvent.LoginSuccess -> {}
            is LoginEvent.LoginFailed -> {}
        }
    }

    fun login(event: Login) {
        if (loginUseCase.isEmailValid(state.email) && loginUseCase.isPasswordValid(state.password)) {
            viewModelScope.launch {
                state = state.copy(isLogginIn = true)
                state = state.copy(isLoginSuccessful = loginUseCase.loginUser(event))
                state = state.copy(isLogginIn = false)

                if (state.isLoginSuccessful) {
                    eventChannel.send(LoginEvent.LoginSuccess(state.email))
                } else {
                    eventChannel.send(LoginEvent.LoginFailed)
                }
            }
        } else {
            Log.e("TAG", "Password or Email was invalid")
        }
    }
}

data class LoginState(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var isLogginIn: Boolean = false,
    var isEmailValid: Boolean = false,
    var isPasswordVisible: Boolean = false,
    var isLoginSuccessful: Boolean = false,
)

