package com.example.taskyy.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.taskyy.ui.ReminderType
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.objects.Day
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(ReminderState())
    val state: StateFlow<ReminderState> = _state.asStateFlow()


    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.SetDateString -> {
                setDateString()
            }

            is ReminderEvent.SaveReminder -> {
            }

            is ReminderEvent.SaveDetails -> {
                _state.update { it.copy(reminderDescription = event.details) }
                //saveDetails()
            }

            is ReminderEvent.Close -> {
                close()
            }

            is ReminderEvent.TimeSelected -> {
                formatTimeSelected(event.selectedTime)
            }

            is ReminderEvent.ReminderDescriptionUpdated -> {
                _state.update { it.copy(reminderDescription = event.reminderDescription) }
            }

            is ReminderEvent.ReminderTitleTextUpdated -> {
                _state.update { it.copy(reminderTitleText = event.reminderTitleText) }
            }

            is ReminderEvent.AlarmTimeTextSelected -> {
                _state.update {
                    it.copy(alarmReminderTimeSelection = setReminderText(event.alarmTimeText))
                }
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

    private fun formatTimeSelected(time: String) {
        var timeString = time
        if (timeString == "0:0") {
            timeString = ("00:00")
        }
        if (timeString == "12:0") {
            timeString = ("12:00")
        }
        val twelveHourDateFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
        val time24 = LocalTime.parse(timeString, twelveHourDateFormat)
        val formatter12 = DateTimeFormatter.ofPattern("hh:mm a")
        val time12 = time24.format(formatter12)

        _state.update { it.copy(selectedTime = time12) }
    }

    private fun save() {
        TODO("Not yet implemented")
    }

    private fun setReminderText(alarmTimeText: ReminderType): String {
        return when (alarmTimeText) {
            ReminderType.ONE_HOUR_BEFORE -> "1 hour before"
            ReminderType.THIRTY_MINUTES_BEFORE -> "30 minutes before"
            ReminderType.FIFTEEN_MINUTES_BEFORE -> "15 minutes before"
            ReminderType.TEN_MINUTES_BEFORE -> "10 minutes before"
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

    private fun setDateString() {
        val date = savedStateHandle.get<String>("dateString")
        _state.update { it.copy(dateString = date ?: "") }
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