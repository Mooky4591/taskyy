package com.example.taskyy.ui.events

import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.objects.AgendaEventItem


sealed interface AgendaEvent {
    data class OnMonthExpanded(val isMonthExpanded: Boolean) : AgendaEvent
    data class OnDateSelected(val date: Long) : AgendaEvent
    data class OnUserInitialsClicked(val isUserDropDownExpanded: Boolean) : AgendaEvent
    data object OnLogOutCLicked : AgendaEvent
    data class AddAgendaItem(val isAgendaItemExpanded: Boolean) : AgendaEvent
    data class OnAgendaDaySelected(val agendaDaySelected: Boolean) : AgendaEvent
    data class UpdateDate(val date: Long) : AgendaEvent
    data object LogoutSuccessful : AgendaEvent
    data class SelectedDayIndex(val index: Int) : AgendaEvent
    data class MenuItemSelected(val itemType: AgendaItemType, val eventItemId: String?) :
        AgendaEvent

    data class EditExistingReminder(val agendaItem: AgendaEventItem) : AgendaEvent
    data object DeleteExistingReminder : AgendaEvent
    data class IsEllipsisMenuExpanded(val isEllipsisMenuExpanded: Boolean) : AgendaEvent
}