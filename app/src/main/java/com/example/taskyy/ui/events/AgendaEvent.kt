package com.example.taskyy.ui.events

sealed interface AgendaEvent {
    data class OnMonthExpanded(val isMonthExpanded: Boolean): AgendaEvent
    data class OnDateSelected(val date: Long): AgendaEvent
    data class OnUserInitialsClicked(val isUserDropDownExpanded: Boolean): AgendaEvent
    data object OnLogOutCLicked: AgendaEvent
}