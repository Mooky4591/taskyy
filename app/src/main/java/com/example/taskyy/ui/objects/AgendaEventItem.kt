package com.example.taskyy.ui.objects

import com.example.taskyy.ui.enums.AgendaItemType

open class AgendaEventItem(
    val title: String,
    val description: String,
    val alarmType: Long,
    val eventId: String,
    val timeInMillis: Long,
    val eventType: AgendaItemType
) {
}
