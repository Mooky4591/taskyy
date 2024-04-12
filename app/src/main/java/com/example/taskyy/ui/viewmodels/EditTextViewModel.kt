package com.example.taskyy.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.events.EditTextEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditTextViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(EditTextState())
    val state: StateFlow<EditTextState> = _state.asStateFlow()

    init {
        val screenType = savedStateHandle.get<EditTextScreenType>("screenType")
        _state.update {
            it.copy(
                screenType = screenType,
                enteredText = savedStateHandle.get<String>("textToEdit") ?: "edit"
            )
        }
    }

    fun onEvent(event: EditTextEvent) {
        when (event) {
            is EditTextEvent.TextUpdated -> {
                _state.update { it.copy(enteredText = event.updatedText) }
            }

            is EditTextEvent.SaveTitle -> {
            }

            is EditTextEvent.SaveDescription -> {
            }

            is EditTextEvent.Back -> {}
        }
    }
}

data class EditTextState(
    var enteredText: String = "",
    var title: String = "",
    var screenType: EditTextScreenType? = null
)