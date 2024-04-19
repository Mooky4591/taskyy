package com.example.taskyy.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.error.asUiText
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.domain.repository.AuthRepository
import com.example.taskyy.domain.repository.UserPreferences
import com.example.taskyy.domain.usecases.LoginUseCase
import com.example.taskyy.ui.UiText
import com.example.taskyy.ui.events.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            when (authRepository.validateToken()) {
                is Result.Error -> {
                }

                is Result.Success -> {
                    if (userPreferences.getUserFullName() != "" && userPreferences.getUserEmail() != ""
                    ) {
                        eventChannel.send(LoginEvent.LoginSuccess(userPreferences.getUserEmail()))
                    }
                }
            }
            state = state.copy(isReady = true)
        }
    }

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
        if (state.email?.let { loginUseCase.isEmailValid(it) } == true) {
            viewModelScope.launch {
                state = state.copy(isLogginIn = true)
                when (val login = loginUseCase.loginUser(event)) {
                    is Result.Success -> {
                        state = state.copy(isLogginIn = false)
                        userPreferences.addUserEmail(email = state.email!!)
                        eventChannel.send(LoginEvent.LoginSuccess(state.email!!))
                    }

                    is Result.Error -> {
                        state = state.copy(loginErrorMessage = login.error.asUiText())
                        state = state.copy(isLogginIn = false)
                        eventChannel.send(LoginEvent.LoginFailed(state.loginErrorMessage!!))
                    }
                }
            }
        }
    }
}

data class LoginState(
    val email: String? = null,
    val password: String? = null,
    val name: String = "",
    val isLogginIn: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val loginErrorMessage: UiText? = null,
    val isReady: Boolean = false
)

