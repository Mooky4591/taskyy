package com.example.taskyy.ui.viewmodels

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.error.asUiText
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.ui.enums.ReminderType
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.objects.Day
import com.example.taskyy.ui.objects.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt


@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val agendaRepository: AgendaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReminderState())
    val state: StateFlow<ReminderState> = _state.asStateFlow()

    private val eventChannel = Channel<ReminderEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        if (savedStateHandle.get<String>("dateString") != null) {
            _state.update {
                it.copy(dateString = savedStateHandle.get<String>("dateString")!!)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.SetDateString -> {
            }

            is ReminderEvent.SaveReminder -> {
                save(event.title, event.description)
            }

            is ReminderEvent.Close -> {
                close()
            }

            is ReminderEvent.TimeSelected -> {
                formatTimeSelected(event.selectedTime)
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

    @OptIn(ExperimentalMaterial3Api::class)
    private fun formatTimeSelected(time: TimePickerState) {

        val localTime = org.joda.time.LocalTime(time.hour, time.minute)
        val twelveHourDateFormat =
            DateTimeFormat.forPattern("hh:mm aa") // 12-hour format with AM/PM
        val formattedTime = twelveHourDateFormat.print(localTime)

        _state.update { it.copy(selectedTime = formattedTime) }
    }

    private fun save(title: String, description: String) {
        val newReminder = Reminder(
            alarmType = _state.value.formattedReminderType,
            title = title,
            description = description,
            timeInMillis = _state.value.formattedReminderTime,
            id = Random.nextInt(1..10000),

            )
        viewModelScope.launch {
            when (val save = agendaRepository.saveReminder(newReminder)) {
                is Result.Success -> {
                    eventChannel.send(ReminderEvent.SaveSuccessful)
                }

                is Result.Error -> {
                    eventChannel.send(ReminderEvent.SaveFailed(save.error.asUiText()))
                }
            }
        }
    }

    private fun formatTimeForDB(selectedTime: String, selectedDate: String) {

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(selectedDate) ?: Date(0)
        val dateMillis = date.time

        val timeFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
        timeFormat.timeZone = TimeZone.getTimeZone("UTC")
        val time = timeFormat.parse(selectedTime) ?: Date(0)
        val timeMillis = time.time

        _state.update { it.copy(formattedReminderTime = dateMillis + timeMillis) }
    }

    private fun formatReminderType(reminderType: String, timeToAdd: Int) {
        val timeToAddInMilis: Long = when (reminderType) {
            "10 minutes before" -> timeToAdd * 60 * 1000L
            "30 minutes before" -> timeToAdd * 60 * 1000L
            "1 hour before" -> timeToAdd * 60 * 60 * 1000L
            "6 hours before" -> timeToAdd * 60 * 60 * 1000L
            "1 day before" -> timeToAdd * 24 * 60 * 60 * 1000L
            else -> {
                0
            }
        }
        _state.update { it.copy(formattedReminderType = timeToAddInMilis) }
    }

    private fun setReminderText(alarmTimeText: ReminderType): String {
        _state.update { it.copy(reminderType = alarmTimeText) }
        var timeText = ""
        when (alarmTimeText) {
            ReminderType.ONE_HOUR_BEFORE -> {
                timeText = "1 hour before"
                formatTimeForDB(
                    selectedTime = _state.value.selectedTime,
                    selectedDate = _state.value.dateString
                )
                formatReminderType(timeText, 1)
            }

            ReminderType.THIRTY_MINUTES_BEFORE -> {
                timeText = "30 minutes before"
                formatTimeForDB(_state.value.selectedTime, _state.value.dateString)
                formatReminderType(timeText, 30)
            }

            ReminderType.ONE_DAY_BEFORE -> {
                timeText = "1 day before"
                formatTimeForDB(_state.value.selectedTime, _state.value.dateString)
                formatReminderType(timeText, 1)
            }

            ReminderType.TEN_MINUTES_BEFORE -> {
                timeText = "10 minutes before"
                formatTimeForDB(_state.value.selectedTime, _state.value.dateString)
                formatReminderType(timeText, 10)
            }

            ReminderType.SIX_HOURS_BEFORE -> {
                timeText = "6 hours before"
                formatTimeForDB(_state.value.selectedTime, _state.value.dateString)
                formatReminderType(timeText, 6)
            }
        }
        return timeText
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

    fun setReminderDescription(description: String) {
        _state.update { it.copy(reminderDescription = description) }
    }

    fun setReminderTitle(title: String) {
        _state.update { it.copy(reminderTitleText = title) }
    }
}


data class ReminderState(
    var dateString: String = "",
    var isAlarmSelectionExpanded: Boolean = false,
    var isTimePickerSelectionExpanded: Boolean = false,
    var isDatePickerExpanded: Boolean = false,
    var alarmReminderTimeSelection: String = "",
    var reminderType: ReminderType = ReminderType.ONE_HOUR_BEFORE,
    var formattedReminderTime: Long = 0,
    var formattedReminderType: Long = 0,
    var selectedTime: String = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")),
    var selectedDate: Long = 0,
    var reminderDescription: String = "Description",
    var reminderTitleText: String = "New Reminder",
    var saveFailedMessage: String = "",

    )