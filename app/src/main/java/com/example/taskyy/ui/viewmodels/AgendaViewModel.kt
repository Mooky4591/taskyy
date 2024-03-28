package com.example.taskyy.ui.viewmodels

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.repository.AgendaRepository
import com.example.taskyy.domain.usecases.LogoutUseCase
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.objects.Day
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
    private val sharedPreferences: SharedPreferences,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var state by mutableStateOf(AgendaState())
        private set

    private val eventChannel = Channel<AgendaEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        setUserInitials()
        setDefaultDateString()
    }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.OnMonthExpanded -> state =
                state.copy(isMonthExpanded = event.isMonthExpanded)

            is AgendaEvent.OnDateSelected -> formatSelectedDate(event.date)
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

            is AgendaEvent.UpdateDateString -> {
                state = state.copy(dateString = event.date)
                savedStateHandle["dateString"] = event.date
            }

            is AgendaEvent.SetUserDefaults -> {
                //  setUserInitials()
                //  setDefaultDateString()
            }

            is AgendaEvent.ReminderItemSelected -> {
            }

            is AgendaEvent.TaskItemSelected -> {
            }

            is AgendaEvent.EventItemSelected -> {
            }
        }
    }

    private fun formatSelectedDate(date: Long) {
        val instant = Instant.ofEpochMilli(date)
        val localDateTime = LocalDateTime.ofInstant(
            instant,
            ZoneId.systemDefault()
        )

        val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)
        val dayOfTheMonthFormatter = DateTimeFormatter.ofPattern("d", Locale.ENGLISH)
        val dayOfTheWeekFormatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)
        val dateStringFormatter = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH)

        state = state.copy(dateString = dateStringFormatter.format(localDateTime.plusDays(1)))

        val days = (1..6).map {
            val date = localDateTime.plusDays(it.toLong())
            Day(
                dayOfTheMonth = dayOfTheMonthFormatter.format(date),
                dayOfTheWeek = dayOfTheWeekFormatter.format(date),
                index = it,
                date = dateStringFormatter.format(date)
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
        val email = sharedPreferences.getString("email", "")
        if (savedStateHandle.get<String>("userInitials") != null) {
            state = state.copy(initials = savedStateHandle.get<String>("userInitials") ?: "")
        } else {
            viewModelScope.launch {
                val name = agendaRepository.getUserName(email!!)
                sharedPreferences.edit().putString("name", name).apply()
                state = state.copy(name = name)
                state = state.copy(initials = name
                    .split(' ')
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .reduce { acc, s -> acc + s })
                savedStateHandle["userInitials"] = state.initials
            }
        }
    }

    private fun setDefaultDateString() {
        if (savedStateHandle.get<String>("dateString") != null) {
            state = state.copy(dateString = savedStateHandle.get<String>("dateString") ?: "")
        } else {
            val dateStringFormatter = DateTimeFormatter.ofPattern("d MMMM uuuu")
            savedStateHandle["dateString"] = dateStringFormatter.format(LocalDateTime.now())
            state =
                savedStateHandle.get<String?>("dateString")?.let { state.copy(dateString = it) }!!
        }
    }
}
data class AgendaState(
    var name: String = "",
    var initials: String = "",
    var isMonthExpanded: Boolean = false,
    var selectedMonth: String = "",
    var selectedDayList: List<Day> = getDefaultListOfDays(),
    var dateString: String = "",
    var isUserDropDownExpanded: Boolean = false,
    var isUserLoggingOut: Boolean = false,
    var wasLogoutSuccessful: Boolean = false,
    var isAddAgendaItemExpanded: Boolean = false,
    var selectedAgendaDay: Boolean = false,
    var selectedIndex: Int = 0
) : Serializable

fun getDefaultListOfDays(): List<Day> {
    val dayOfTheMonthFormatter = DateTimeFormatter.ofPattern("d", Locale.ENGLISH)
    val dayOfTheWeekFormatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)
    val dateStringFormatter = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH)

    return (0..5).map {
        val date = LocalDateTime.now().plusDays(it.toLong())
        Day(
            dayOfTheMonth = dayOfTheMonthFormatter.format(date),
            dayOfTheWeek = dayOfTheWeekFormatter.format(date),
            index = it,
            date = dateStringFormatter.format(date)
        )
    }
}
