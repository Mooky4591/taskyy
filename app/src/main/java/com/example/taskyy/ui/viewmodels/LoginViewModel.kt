package com.example.taskyy.ui.viewmodels

import androidx.compose.ui.input.key.Key.Companion.T
import androidx.lifecycle.ViewModel
import com.example.taskyy.domain.repository.AuthRepository
import com.example.taskyy.ui.events.LoginEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(T)
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvents) {
        when(event) {
            //is KeyEvent
        }
    }

}

