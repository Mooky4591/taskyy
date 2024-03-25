package com.example.taskyy.ui.events

interface ReminderEvent {

    data object SetDateString : ReminderEvent
    data class AlarmTypeDropDownSelected(var isAlarmSelectionExpanded: Boolean) : ReminderEvent
    data class TimePickerSelected(var timePickerSelected: Boolean) : ReminderEvent
    data class AlarmTimeTextSelected(var alarmTimeText: String) : ReminderEvent
    data class TimeSelected(var selectedTime: String) : ReminderEvent
    data class ReminderDescriptionUpdated(var reminderDescription: String) : ReminderEvent
    data class ReminderTitleTextUpdated(var reminderTitleText: String) : ReminderEvent
    data class UpdateDateSelection(var selectedDate: Long) : ReminderEvent
    data class DatePickerSelcted(var datePickerExpanded: Boolean) : ReminderEvent
    data object SaveSelected : ReminderEvent
    data object Close : ReminderEvent
}