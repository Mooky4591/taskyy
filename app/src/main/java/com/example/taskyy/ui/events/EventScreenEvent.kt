package com.example.taskyy.ui.events

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.example.taskyy.ui.UiText
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.enums.ReminderType

interface EventScreenEvent {
    data object SetDateString : EventScreenEvent
    data class AlarmTypeDropDownSelected(var isAlarmSelectionExpanded: Boolean) : EventScreenEvent
    data class TimePickerSelected(var timePickerSelected: Boolean) : EventScreenEvent
    data class AlarmTimeTextSelected(var alarmTimeText: ReminderType) : EventScreenEvent
    data class TimeSelected @OptIn(ExperimentalMaterial3Api::class) constructor(var selectedTime: TimePickerState) :
        EventScreenEvent

    data class EnterEventDescription(var editDescription: EditTextScreenType) : EventScreenEvent
    data class EnterSetTitleScreen(var editTitle: EditTextScreenType) : EventScreenEvent
    data class EventTitleTextUpdated(var reminderTitleText: String) : EventScreenEvent
    data class UpdateDateSelection(var selectedDate: Long) : EventScreenEvent
    data class DatePickerSelcted(var datePickerExpanded: Boolean) : EventScreenEvent
    data class SaveEvent(
        var title: String,
        var description: String,
        var isDone: Boolean?
    ) : EventScreenEvent

    data class UpdateEvent(
        var eventId: String,
        var title: String,
        var description: String
    ) : EventScreenEvent

    data object Close : EventScreenEvent
    data object SaveSuccessful : EventScreenEvent
    data class SaveFailed(val errorMessage: UiText) : EventScreenEvent
}