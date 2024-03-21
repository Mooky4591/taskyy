package com.example.taskyy.ui.events

import com.example.taskyy.domain.objects.Login


sealed interface LoginEvent {
    data class OnEmailChanged(val email: String) : LoginEvent
    data class OnPasswordChanged(val password: String) : LoginEvent
    data class OnTogglePasswordVisibility(val isPasswordVisible: Boolean) : LoginEvent
    data class OnNameChanged(val name: String) : LoginEvent
    data class OnLoginClick(val login: Login) : LoginEvent
    data object OnRegisterLinkClick : LoginEvent
    data object LoginFailed : LoginEvent
    data class LoginSuccess(val email: String) : LoginEvent
}