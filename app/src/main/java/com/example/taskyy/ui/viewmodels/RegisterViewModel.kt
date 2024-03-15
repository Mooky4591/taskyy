package com.example.taskyy.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.objects.User
import com.example.taskyy.domain.usecases.RegisterUseCase
import com.example.taskyy.ui.events.RegisterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
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
        }
    }

    private fun register() {
        val user = User(fullName = state.name, password = state.password, email = state.email)
        if (registerUseCase.isPasswordValid(user.password)) {
            viewModelScope.launch {
                state = state.copy(isRegistrationSuccessful = registerUseCase.registerUser(user))
                if (state.isRegistrationSuccessful) {
                    eventChannel.send(RegisterEvent.RegistrationSuccessful)
                }
            }
        } else {
            Log.e("TAG", "Password in invalid")
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
        var isLoading: Boolean = false
    )
