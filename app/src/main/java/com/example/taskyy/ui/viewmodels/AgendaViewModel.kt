package com.example.taskyy.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.usecases.LogoutUseCase
import com.example.taskyy.ui.events.AgendaEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
): ViewModel() {
    var state by mutableStateOf(AgendaState())
        private set

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.OnMonthExpanded -> state = state.copy(isMonthExpanded = event.isMonthExpanded)
            is AgendaEvent.OnDateSelected -> formatSelectedDate(event.date)
            is AgendaEvent.OnUserInitialsClicked -> state = state.copy(isUserDropDownExpanded = event.isUserDropDownExpanded)
            is AgendaEvent.OnLogOutCLicked -> logout()
        }
    }

    private fun formatSelectedDate(date: Long) {
        val monthDate = SimpleDateFormat("MMMM", Locale.US)
        val monthName = monthDate.format(date)
        state = state.copy(selectedMonth = monthName)
    }

    private fun logout() {
        viewModelScope.launch {
            state = state.copy(isUserLoggingOut = true)
            state = state.copy(wasLogoutSuccessful = logoutUseCase.logout())
            state = state.copy(isUserLoggingOut = false)
        }
    }
}
data class AgendaState(
    var name: String = "",
    var isMonthExpanded: Boolean = false,
    var selectedMonth: String = "",
    var isUserDropDownExpanded: Boolean = false,
    var isUserLoggingOut: Boolean = false,
    var wasLogoutSuccessful: Boolean = false
)
