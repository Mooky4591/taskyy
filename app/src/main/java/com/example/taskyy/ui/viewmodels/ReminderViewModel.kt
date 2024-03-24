package com.example.taskyy.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.objects.Day
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(ReminderState())
    val state: StateFlow<ReminderState> = _state.asStateFlow()


    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.SetDateString -> setDateString()
            is ReminderEvent.SaveSelected -> {
                save()
            }

            is ReminderEvent.Close -> {
                close()
            }

            is ReminderEvent.TimeSelected -> {
                _state.update { it.copy(selectedTime = event.selectedTime) }
            }

            is ReminderEvent.ReminderDescriptionUpdated -> {
                _state.update { it.copy(reminderDescription = event.reminderDescription) }
            }

            is ReminderEvent.ReminderTitleTextUpdated -> {
                _state.update { it.copy(reminderTitleText = event.reminderTitleText) }
            }

            is ReminderEvent.AlarmTimeTextSelected -> {
                _state.update { it.copy(alarmReminderTimeSelection = event.alarmTimeText) }
            }

            is ReminderEvent.AlarmTypeDropDownSelected -> {
                _state.update { it.copy(isAlarmSelectionExpanded = event.isAlarmSelectionExpanded) }
            }

            is ReminderEvent.TimePickerSelected -> {
                _state.update { it.copy(isTimePickerSelectionExpanded = event.timePickerSelected) }
            }

            is ReminderEvent.DatePickerSelcted -> {
                _state.update { it.copy(isDatePickerExpanded = event.datePickerExpanded) }
            }

            is ReminderEvent.UpdateDateSelection -> formatDateString(event.selectedDate)
        }
    }

    private fun formatDateString(date: Long) {
        val instant = Instant.ofEpochMilli(date)
        val localDateTime = LocalDateTime.ofInstant(
            instant,
            ZoneId.systemDefault()
        )

        val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)
        val dayOfTheMonthFormatter = DateTimeFormatter.ofPattern("d", Locale.ENGLISH)
        val dayOfTheWeekFormatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)
        val dateStringFormatter = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH)

        _state.update { it.copy(dateString = dateStringFormatter.format(localDateTime.plusDays(1))) }

        val days = (1..6).map {
            val date = localDateTime.plusDays(it.toLong())
            Day(
                dayOfTheMonth = dayOfTheMonthFormatter.format(date),
                dayOfTheWeek = dayOfTheWeekFormatter.format(date),
                index = it,
                date = dateStringFormatter.format(date)
            )
        }

        savedStateHandle["days"] = days
        savedStateHandle["selectedMonth"] = monthFormatter.format(localDateTime).uppercase()
    }

    private fun close() {
        TODO("Not yet implemented")
    }

    private fun save() {
        TODO("Not yet implemented")
    }

    private fun setDateString() {
        val state = savedStateHandle.get<AgendaState>("state")
        _state.update { it.copy(dateString = state?.dateString ?: "") }
    }
}

data class ReminderState(
    var dateString: String = "",
    var isAlarmSelectionExpanded: Boolean = false,
    var isTimePickerSelectionExpanded: Boolean = false,
    var isDatePickerExpanded: Boolean = false,
    var alarmReminderTimeSelection: String = "1 hour before",
    var selectedTime: String = "",
    var selectedDate: Long = 0,
    var reminderDescription: String = "Reminder description",
    var reminderTitleText: String = "New Reminder"
)