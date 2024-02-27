package com.example.taskyy.ui.events

import android.content.Context


sealed interface RegisterEvent{
    data class OnNameChanged(val name: String): RegisterEvent
    data class OnEmailChanged(val email: String): RegisterEvent
    data class OnPasswordChanged(val password: String): RegisterEvent
    data class OnTogglePasswordVisibility(val isPasswordVisible: Boolean): RegisterEvent

    data class OnGetStartedClick(val applicationContext: Context) : RegisterEvent

}