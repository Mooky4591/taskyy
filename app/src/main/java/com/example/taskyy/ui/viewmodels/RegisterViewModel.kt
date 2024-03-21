package com.example.taskyy.ui.viewmodels

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.error.asErrorUiText
import com.example.taskyy.domain.error.asUiText
import com.example.taskyy.domain.objects.User
import com.example.taskyy.domain.usecases.RegisterUseCase
import com.example.taskyy.ui.UiText
import com.example.taskyy.ui.events.RegisterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val sharedPreferences: SharedPreferences
):ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set
    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnNameChanged -> state = state.copy(name = event.name)
            is RegisterEvent.OnEmailChanged -> state = state.copy(
                email = event.email,
                isEmailValid = registerUseCase.isEmailValid(event.email)
            )

            is RegisterEvent.OnPasswordChanged -> state = state.copy(password = event.password)
            is RegisterEvent.OnGetStartedClick -> register()
            is RegisterEvent.OnTogglePasswordVisibility -> state =
                state.copy(isPasswordVisible = event.isPasswordVisible)

            is RegisterEvent.RegistrationSuccessful -> {}
            is RegisterEvent.RegistrationFailed -> {}
        }
    }

    private fun register() {
        val user = User(fullName = state.name, password = state.password, email = state.email)
        if (validatePassword(user.password)) {
            viewModelScope.launch {
                when (val result = registerUseCase.registerUser(user)) {
                    is Result.Error -> {
                        state = state.copy(isRegistrationSuccessful = false)
                        state = state.copy(
                            networkErrorMessage = result.error.asUiText()
                        )
                        eventChannel.send(RegisterEvent.RegistrationFailed(state.networkErrorMessage!!))
                    }

                    is Result.Success -> {
                        sharedPreferences.edit().putString("name", user.fullName).apply()
                        state = state.copy(isRegistrationSuccessful = true)
                        eventChannel.send(RegisterEvent.RegistrationSuccessful)
                    }
                }
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return when (val result = registerUseCase.isPasswordValid(password)) {
            is Result.Error -> {
                state = state.copy(
                    passwordInvalidErrorMessage = result.asErrorUiText()
                )
                false
            }

            is Result.Success -> {
                state = state.copy(passwordInvalidErrorMessage = null)
                true
            }
        }
    }
}

data class RegisterState(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var isEmailValid: Boolean = false,
    var isPasswordVisible: Boolean = false,
    var isRegistrationSuccessful: Boolean = false,
    var isLoginSuccessful: Boolean = false,
    var isLoading: Boolean = false,
    var passwordInvalidErrorMessage: UiText? = null,
    var networkErrorMessage: UiText? = null
)
