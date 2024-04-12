package com.example.taskyy.ui.objects

import com.example.taskyy.ui.enums.AgendaItemType

class Reminder(
    title: String,
    description: String,
    timeInMillis: Long,
    alarmType: Long,
    id: String,
    agendaItem: AgendaItemType
) : AgendaEventItem(title, description, alarmType, id, timeInMillis, agendaItem) {

}