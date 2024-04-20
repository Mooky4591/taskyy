package com.example.taskyy.domain.objects

import com.example.taskyy.ui.objects.AgendaEventItem

data class FullAgenda(
    val events: List<AgendaEventItem>,
    val tasks: List<AgendaEventItem>,
    val reminders: List<AgendaEventItem>
)
