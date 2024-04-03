package com.example.taskyy.ui.viewmodels

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.error.asUiText
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.domain.repository.UserPreferences
import com.example.taskyy.ui.enums.ReminderType
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.objects.Day
import com.example.taskyy.ui.objects.Reminder
import com.example.taskyy.ui.screens.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit


@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val agendaRepository: AgendaRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(ReminderState())
    val state: StateFlow<ReminderState> = _state.asStateFlow()

    private val _dateTimeState = MutableStateFlow(TimeAndDateState())
    val timeAndDateState: StateFlow<TimeAndDateState> = _dateTimeState.asStateFlow()

    private val eventChannel = Channel<ReminderEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        if (savedStateHandle.get<String>("dateString") != null) {
            val date = savedStateHandle.get<String>("dateString")!!
            formatDateTimeStringFromSavedStateHandle(date)
        }
    }

    private fun formatDateTimeStringFromSavedStateHandle(date: String) {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val localDateTime = LocalDateTime.parse(date, formatter)
        _dateTimeState.update { it.copy(dateTime = localDateTime) }
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

            is ReminderEvent.UpdateDateSelection -> {
                formatDateString(event.selectedDate)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun formatTimeSelected(time: TimePickerState) {

        val localTime = LocalTime.of(time.hour, time.minute)

        _dateTimeState.update { it.copy(dateTime = localTime.atDate(_dateTimeState.value.dateTime.toLocalDate())) }
    }

    private fun save(title: String, description: String) {
        val userId = userPreferences.getUserId("userId")
        val newReminder =
            Reminder(
                alarmType = _state.value.formattedReminderTime,
                title = title,
                description = description,
                timeInMillis = _dateTimeState.value.dateTime.toLocalDate()
                    .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                id = UUID.randomUUID().toString(),
                userId = userId
            )
        viewModelScope.launch {
            when (val save = agendaRepository.saveReminderToDB(newReminder)) {
                is Result.Success -> {
                    when (val save = agendaRepository.uploadReminderToApi(newReminder)) {
                        is Result.Success -> {
                            eventChannel.send(ReminderEvent.SaveSuccessful)
                        }

                        is Result.Error -> {
                            eventChannel.send(ReminderEvent.SaveFailed(save.error.asUiText()))
                        }
                    }
                }
                is Result.Error -> {
                    eventChannel.send(ReminderEvent.SaveFailed(save.error.asUiText()))
                }
            }
        }
    }

    private fun formatReminderTimeForDB(selectedTime: String, selectedDate: LocalDateTime) {
        when (selectedTime) {
            "10 minutes before" -> {
                _state.update {
                    it.copy(
                        formattedReminderTime = selectedDate.plusMinutes(
                            10.minutes.toLong(
                                DurationUnit.MILLISECONDS
                            )
                        ).toMillis()
                    )
                }
            }

            "30 minutes before" -> {
                _state.update {
                    it.copy(
                        formattedReminderTime = selectedDate.plusMinutes(
                            30.minutes.toLong(
                                DurationUnit.MILLISECONDS
                            )
                        ).toMillis()
                    )
                }
            }

            "1 hour before" -> {
                _state.update {
                    it.copy(
                        formattedReminderTime = selectedDate.plusHours(
                            1.hours.toLong(
                                DurationUnit.MILLISECONDS
                            )
                        ).toMillis()
                    )
                }
            }

            "6 hours before" -> {
                _state.update {
                    it.copy(
                        formattedReminderTime = selectedDate.plusHours(
                            6.hours.toLong(
                                DurationUnit.MILLISECONDS
                            )
                        ).toMillis()
                    )
                }
            }

            "1 day before" -> {
                _state.update {
                    it.copy(
                        formattedReminderTime = selectedDate.plusDays(
                            1.days.toLong(
                                DurationUnit.MILLISECONDS
                            )
                        ).toMillis()
                    )
                }
            }
        }
    }

    private fun setReminderText(alarmTimeText: ReminderType): String {
        _state.update { it.copy(reminderType = alarmTimeText) }
        var timeText = ""
        when (alarmTimeText) {
            ReminderType.ONE_HOUR_BEFORE -> {
                timeText = "1 hour before"
                formatReminderTimeForDB(timeText, _dateTimeState.value.dateTime)
            }

            ReminderType.THIRTY_MINUTES_BEFORE -> {
                timeText = "30 minutes before"
                formatReminderTimeForDB(timeText, _dateTimeState.value.dateTime)
            }

            ReminderType.ONE_DAY_BEFORE -> {
                timeText = "1 day before"
                formatReminderTimeForDB(timeText, _dateTimeState.value.dateTime)
            }

            ReminderType.TEN_MINUTES_BEFORE -> {
                timeText = "10 minutes before"
                formatReminderTimeForDB(timeText, _dateTimeState.value.dateTime)
            }

            ReminderType.SIX_HOURS_BEFORE -> {
                timeText = "6 hours before"
                formatReminderTimeForDB(timeText, _dateTimeState.value.dateTime)
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

        _dateTimeState.update {
            it.copy(
                dateTime = localDateTime
                    .plusHours(_dateTimeState.value.dateTime.hour.toLong())
                    .plusMinutes(_dateTimeState.value.dateTime.minute.toLong())
                    .plusSeconds(_dateTimeState.value.dateTime.second.toLong())
            )
        }

        val days = (1..6).map {
            val date = localDateTime.plusDays(it.toLong())
            Day(
                dayOfTheMonth = dayOfTheMonthFormatter.format(date),
                dayOfTheWeek = dayOfTheWeekFormatter.format(date),
                index = it,
                date = date.toMillis()
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
    var isAlarmSelectionExpanded: Boolean = false,
    var isTimePickerSelectionExpanded: Boolean = false,
    var isDatePickerExpanded: Boolean = false,
    var alarmReminderTimeSelection: String = "",
    var reminderType: ReminderType = ReminderType.ONE_HOUR_BEFORE,
    var formattedReminderTime: Long = 3600000,
    var reminderDescription: String = "Description",
    var reminderTitleText: String = "New Reminder",
    var saveFailedMessage: String = "",
)

data class TimeAndDateState(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    )