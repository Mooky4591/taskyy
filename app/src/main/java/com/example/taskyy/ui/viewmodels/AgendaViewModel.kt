package com.example.taskyy.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.error.Result
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.domain.repository.UserPreferences
import com.example.taskyy.domain.usecases.CheckForRemindersUseCase
import com.example.taskyy.domain.usecases.LogoutUseCase
import com.example.taskyy.ui.enums.AgendaItemAction
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.objects.AgendaEventItem
import com.example.taskyy.ui.objects.Day
import com.example.taskyy.ui.objects.Reminder
import com.example.taskyy.ui.screens.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val agendaRepository: AgendaRepository,
    private val userPreferences: UserPreferences,
    private val savedStateHandle: SavedStateHandle,
    private val checkForRemindersUseCase: CheckForRemindersUseCase
): ViewModel() {
    var state by mutableStateOf(AgendaState())
        private set
    var timeDateState by mutableStateOf(TimeDateState())
        private set

    private val eventChannel = Channel<AgendaEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        setUserInitials()
            checkForReminders(
                LocalDateTime.now()
            )
        startWorkManager(Context.)
        }

    private fun checkForReminders(dateTime: LocalDateTime) {
        viewModelScope.launch {
            when (val reminderList = checkForRemindersUseCase.checkForReminders(dateTime)) {
                is Result.Success -> {
                    state = state.copy(listOfAgendaEvents = reminderList.data)
                }
                is Result.Error -> {
                }
            }
        }
    }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.OnMonthExpanded -> state =
                state.copy(isMonthExpanded = event.isMonthExpanded)

            is AgendaEvent.OnDateSelected -> {
                formatSelectedDate(event.date)
                generateSelectableDaysRow(event.date)
            }
            is AgendaEvent.OnUserInitialsClicked -> state =
                state.copy(isUserDropDownExpanded = event.isUserDropDownExpanded)

            is AgendaEvent.OnLogOutCLicked -> logout()
            is AgendaEvent.AddAgendaItem -> state =
                state.copy(isAddAgendaItemExpanded = event.isAgendaItemExpanded)

            is AgendaEvent.OnAgendaDaySelected -> state =
                state.copy(selectedAgendaDay = event.agendaDaySelected)

            is AgendaEvent.LogoutSuccessful -> {}
            is AgendaEvent.SelectedDayIndex -> state =
                state.copy(selectedIndex = event.index)

            is AgendaEvent.MenuItemSelected -> {
            }
            is AgendaEvent.UpdateDate -> {
                formatSelectedDate(event.date)
            }

            is AgendaEvent.IsEllipsisMenuExpanded -> {
                state =
                    state.copy(isEllipsisMenuExpanded = event.isEllipsisMenuExpanded)
            }

            is AgendaEvent.DeleteExistingReminder -> {
                deleteReminder(event.agendaEventItem)
            }
            is AgendaEvent.EditExistingReminder -> {}
        }
    }

    private fun startWorkManager(context: Context) {
        viewModelScope.launch {
            agendaRepository.startWorkManager(context)
        }
    }

    private fun deleteReminder(agendaEventItem: AgendaEventItem) {
        agendaEventItem.agendaAction = AgendaItemAction.DELETE
        viewModelScope.launch {
            when (val delete = agendaRepository.deleteReminderInDb(agendaEventItem)) {
                is Result.Success -> {
                    checkForReminders(timeDateState.dateTime)
                    when (val delete = agendaRepository.deleteReminderOnApi(agendaEventItem)) {
                        is Result.Success -> {
                        }

                        is Result.Error -> {
                        }
                    }
                }
                is Result.Error -> {
                }
            }
        }
    }

    private fun formatSelectedDate(date: Long) {
        val instant = Instant.ofEpochMilli(date)
        val localDateTime = LocalDateTime.ofInstant(
            instant,
            ZoneId.systemDefault()
        )
        timeDateState = timeDateState.copy(dateTime = localDateTime)
        checkForReminders(timeDateState.dateTime)

    }

    private fun generateSelectableDaysRow(date: Long) {
        val instant = Instant.ofEpochMilli(date)
        val localDateTime = LocalDateTime.ofInstant(
            instant,
            ZoneId.systemDefault()
        )

        val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)
        val dayOfTheMonthFormatter = DateTimeFormatter.ofPattern("d", Locale.ENGLISH)
        val dayOfTheWeekFormatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)

        val days = (0..5).map {
            val date = localDateTime.plusDays(it.toLong())
            Day(
                dayOfTheMonth = dayOfTheMonthFormatter.format(date),
                dayOfTheWeek = dayOfTheWeekFormatter.format(date),
                index = it,
                date = date.toMillis()
            )
        }

        state = state.copy(
            selectedMonth = monthFormatter.format(localDateTime).uppercase(),
            selectedDayList = days,
        )
        savedStateHandle["days"] = days
        savedStateHandle["selectedMonth"] = monthFormatter.format(localDateTime).uppercase()
    }

    private fun logout() {
        viewModelScope.launch {
            state = state.copy(isUserLoggingOut = true)
            state = state.copy(wasLogoutSuccessful = logoutUseCase.logout())
            state = state.copy(isUserLoggingOut = false)

            if (state.wasLogoutSuccessful) {
                eventChannel.send(AgendaEvent.LogoutSuccessful)
            }
        }
    }

    private fun setUserInitials() {
            viewModelScope.launch {
                val email = userPreferences.getUserEmail("email")
                var name: String = ""
                when (userPreferences.isTokenValid("token")) {
                    is Result.Error -> {
                        name = agendaRepository.getUserName(email)
                        userPreferences.addUserFullName(name, "name")
                    }

                    is Result.Success -> {
                        name = userPreferences.getUserId("name")
                    }
                }

                state = state.copy(name = name)
                state = state.copy(initials = name
                    .split(' ')
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .reduce { acc, s -> acc + s })
                savedStateHandle["userInitials"] = state.initials
            }
        }
    }

private fun AgendaEventItem.toReminder(): Reminder {
    return Reminder(
        id = eventId,
        description = description,
        agendaAction = agendaAction,
        agendaItem = AgendaItemType.REMINDER_ITEM,
        title = title,
        timeInMillis = timeInMillis,
        alarmType = alarmType
    )
}

data class AgendaState(
    var name: String = "",
    var initials: String = "",
    var isMonthExpanded: Boolean = false,
    var selectedMonth: String = "",
    var selectedDayList: List<Day> = getDefaultListOfDays(),
    var isUserDropDownExpanded: Boolean = false,
    var isUserLoggingOut: Boolean = false,
    var wasLogoutSuccessful: Boolean = false,
    var isAddAgendaItemExpanded: Boolean = false,
    var selectedAgendaDay: Boolean = false,
    var selectedIndex: Int = 0,
    var listOfAgendaEvents: List<AgendaEventItem> = listOf<AgendaEventItem>(),
    var isEllipsisMenuExpanded: Boolean = false
) : Serializable

data class TimeDateState(
    val dateTime: LocalDateTime = LocalDateTime.now(),

    )

fun getDefaultListOfDays(): List<Day> {
    val dayOfTheMonthFormatter = DateTimeFormatter.ofPattern("d", Locale.ENGLISH)
    val dayOfTheWeekFormatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)

    return (0..5).map {
        val date = LocalDateTime.now().plusDays(it.toLong())
        Day(
            dayOfTheMonth = dayOfTheMonthFormatter.format(date),
            dayOfTheWeek = dayOfTheWeekFormatter.format(date),
            index = it,
            date = date.toMillis()
        )
    }
}
