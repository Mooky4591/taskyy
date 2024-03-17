package com.example.taskyy.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskyy.domain.usecases.LogoutUseCase
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.objects.Day
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

        val tempDateList: ArrayList<LocalDateTime> = java.util.ArrayList()
        val dayList: ArrayList<Day> = ArrayList()

        val dateList: ArrayList<LocalDateTime> = incrementDate(localDateTime, tempDateList)

        for (date in dateList) {
            val day = Day(
                dayOfTheWeek = dayOfTheWeekFormatter.format(date),
                dayOfTheMonth = dayOfTheMonthFormatter.format(date)
            )
            dayList.add(day)
        }


        state = state.copy(
            selectedMonth = monthFormatter.format(localDateTime).uppercase(),
            selectedDay = dayList,
        )
    }

    private fun incrementDate(
        localDateTime: LocalDateTime,
        dateList: ArrayList<LocalDateTime>,
    ): ArrayList<LocalDateTime> {
        for (i in 0..6) {
            dateList.add(localDateTime.plusDays(i.toLong()))
        }
        return dateList
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
    var selectedDay: java.util.ArrayList<Day> = java.util.ArrayList(),
    var isUserDropDownExpanded: Boolean = false,
    var isUserLoggingOut: Boolean = false,
    var wasLogoutSuccessful: Boolean = false,
    var isAddAgendaItemExpanded: Boolean = false,
    var selectedAgendaDay: Boolean = false
)
