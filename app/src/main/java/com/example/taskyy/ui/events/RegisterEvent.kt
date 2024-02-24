package com.example.taskyy.ui.events

sealed interface RegisterEvent {
    data class OnNameFieldChanged(var name: String): RegisterEvent
}