package com.example.taskyy.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskyy.domain.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    registerRepository: RegisterRepository
): ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set
}

data class RegisterState(
    var email: String = "",
    val password: String = "",
    val isLogginIn: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordVisible: Boolean = false
) {}