package com.example.taskyy.ui.viewmodels

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskyy.ui.events.ReminderEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    var state by mutableStateOf(ReminderState())
        private set

    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.SetDateString -> setDateString()
        }
    }

    private fun setDateString() {
        val dateString = sharedPreferences.getString("date_string", "")
        state = state.copy(dateString = dateString!!)
    }
}

data class ReminderState(
    var dateString: String = ""
)