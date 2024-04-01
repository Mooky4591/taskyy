package com.example.taskyy.ui.events

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.example.taskyy.ui.UiText
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.enums.ReminderType

interface ReminderEvent {

    data object SetDateString : ReminderEvent
    data class AlarmTypeDropDownSelected(var isAlarmSelectionExpanded: Boolean) : ReminderEvent
    data class TimePickerSelected(var timePickerSelected: Boolean) : ReminderEvent
    data class AlarmTimeTextSelected(var alarmTimeText: ReminderType) : ReminderEvent
    data class TimeSelected @OptIn(ExperimentalMaterial3Api::class) constructor(var selectedTime: TimePickerState) :
        ReminderEvent

    data class EnterReminderDescription(var editDescription: EditTextScreenType) : ReminderEvent
    data class EnterSetTitleScreen(var editTitle: EditTextScreenType) : ReminderEvent
    data class ReminderTitleTextUpdated(var reminderTitleText: String) : ReminderEvent
    data class UpdateDateSelection(var selectedDate: Long) : ReminderEvent
    data class DatePickerSelcted(var datePickerExpanded: Boolean) : ReminderEvent
    data class SaveReminder(
        var title: String,
        var description: String
    ) : ReminderEvent

    data object Close : ReminderEvent
    data object SaveSuccessful : ReminderEvent
    data class SaveFailed(val errorMessage: UiText) : ReminderEvent
}