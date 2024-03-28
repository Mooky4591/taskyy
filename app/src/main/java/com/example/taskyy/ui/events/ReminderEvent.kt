package com.example.taskyy.ui.events

import com.example.taskyy.ui.ReminderType

interface ReminderEvent {

    data object SetDateString : ReminderEvent
    data class AlarmTypeDropDownSelected(var isAlarmSelectionExpanded: Boolean) : ReminderEvent
    data class TimePickerSelected(var timePickerSelected: Boolean) : ReminderEvent
    data class AlarmTimeTextSelected(var alarmTimeText: ReminderType) : ReminderEvent
    data class TimeSelected(var selectedTime: String) : ReminderEvent
    data class ReminderDescriptionUpdated(var reminderDescription: String) : ReminderEvent
    data object EnterReminderDescription : ReminderEvent
    data class ReminderTitleTextUpdated(var reminderTitleText: String) : ReminderEvent
    data class UpdateDateSelection(var selectedDate: Long) : ReminderEvent
    data class DatePickerSelcted(var datePickerExpanded: Boolean) : ReminderEvent
    data object SaveReminder : ReminderEvent
    data class SaveDetails(val details: String) : ReminderEvent
    data class SaveTitle(val title: String) : ReminderEvent
    data object Close : ReminderEvent
}