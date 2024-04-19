package com.example.taskyy.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.enums.ReminderType
import com.example.taskyy.ui.events.EventScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.Serializable
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(EventState())
    val state: StateFlow<EventState> = _state.asStateFlow()

    private val _dateTimeState = MutableStateFlow(DateTimeState())
    val timeAndDateState: StateFlow<DateTimeState> = _dateTimeState.asStateFlow()

    private val eventChannel = Channel<EventScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onEvent(event: EventScreenEvent) {
        when (event) {
            is EventScreenEvent.SetDateString -> {

            }

            is EventScreenEvent.Close -> {

            }

            is EventScreenEvent.EnterSetTitleScreen -> {

            }

            is EventScreenEvent.SaveSuccessful -> {

            }

            is EventScreenEvent.AlarmTimeTextSelected -> {

            }

            is EventScreenEvent.AlarmTypeDropDownSelected -> {

            }

            is EventScreenEvent.EnterEventDescription -> {

            }
        }
    }


}

data class EventState(
    val isAlarmSelectionExpanded: Boolean = false,
    val isTimePickerSelectionExpanded: Boolean = false,
    val isDatePickerExpanded: Boolean = false,
    val alarmReminderTimeSelection: String = "",
    val reminderType: ReminderType = ReminderType.ONE_HOUR_BEFORE,
    val formattedReminderTime: Long = 3600000,
    val description: String = "Description",
    val titleText: String = "New Event",
    val saveFailedMessage: String = "",
    val agendaItemType: AgendaItemType = AgendaItemType.EVENT_ITEM,
    val eventId: String = "",
    val isEditingEvent: Boolean = true,
    val isDone: Boolean = false
) : Serializable

data class DateTimeState(
    val dateTime: LocalDateTime = LocalDateTime.now()
) : Serializable