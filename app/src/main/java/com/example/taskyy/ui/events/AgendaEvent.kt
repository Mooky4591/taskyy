package com.example.taskyy.ui.events


sealed interface AgendaEvent {
    data class OnMonthExpanded(val isMonthExpanded: Boolean) : AgendaEvent
    data class OnDateSelected(val date: Long) : AgendaEvent
    data class OnUserInitialsClicked(val isUserDropDownExpanded: Boolean) : AgendaEvent
    data object OnLogOutCLicked : AgendaEvent
    data class AddAgendaItem(val isAgendaItemExpanded: Boolean) : AgendaEvent
    data class OnAgendaDaySelected(val agendaDaySelected: Boolean) : AgendaEvent
    data object LogoutSuccessful : AgendaEvent
    data class SelectedDayIndex(val index: Int) : AgendaEvent
    data class UpdateDateString(val date: String) : AgendaEvent
    data object SetUserInitials : AgendaEvent
}