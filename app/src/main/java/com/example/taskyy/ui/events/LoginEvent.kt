package com.example.taskyy.ui.events

sealed interface LoginEvent {
    data class OnEmailChanged(val email: String): LoginEvent
    data class OnPasswordChanged(val password: String): LoginEvent
    data class OnTogglePasswordVisibility(val isPasswordVisible: Boolean): LoginEvent
    data object OnLoginClick: LoginEvent
    data object OnSignUpClick: LoginEvent
}