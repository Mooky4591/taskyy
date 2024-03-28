package com.example.taskyy.ui.events

interface EditTextEvent {
    data class TextUpdated(var updatedText: String) : EditTextEvent
    data class SaveDescription(var updatedText: String) : EditTextEvent
    data class SaveTitle(var updatedText: String) : EditTextEvent
    data object Back : EditTextEvent

}