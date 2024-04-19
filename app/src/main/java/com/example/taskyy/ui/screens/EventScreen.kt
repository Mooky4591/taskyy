package com.example.taskyy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.enums.ReminderType
import com.example.taskyy.ui.events.EventScreenEvent
import com.example.taskyy.ui.viewmodels.DateTimeState
import com.example.taskyy.ui.viewmodels.EventState
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    state: EventState,
    onEvent: (EventScreenEvent) -> Unit,
    timeDateState: DateTimeState
) {

    Scaffold {
        val formattedTitleDate = remember(timeDateState.dateTime) {
            DateTimeFormatter.ofPattern("dd MMMM yyyy").format(timeDateState.dateTime)
        }
        val formattedTime = remember(timeDateState.dateTime) {
            DateTimeFormatter.ofPattern("HH:mm a").format(timeDateState.dateTime)
        }
        val formattedDateForDatePicker = remember(timeDateState.dateTime) {
            DateTimeFormatter.ofPattern("MMM dd yyyy").format(timeDateState.dateTime)
        }
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr))
        ) {
            TopBar(
                title = formattedTitleDate,
                color = Color.White,
                saveFunction = {
                    SaveEvent(
                        saveEvent = { event ->
                            onEvent(event)
                        },
                        updateEvent = { event ->
                            onEvent(event)
                        },
                        title = state.titleText,
                        description = state.description,
                        isEventBeingEdited = state.isEditingEvent,
                        eventId = state.eventId
                    )
                },
                onClose = { onEvent(EventScreenEvent.Close) }
            )
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
            ) {
                EventScreenContent(
                    enterItemDescription = {
                        onEvent(
                            EventScreenEvent.EnterEventDescription(
                                EditTextScreenType.EDIT_DETAILS
                            )
                        )
                    },
                    onDateExpanded = { onEvent(EventScreenEvent.DatePickerSelcted(datePickerExpanded = !state.isDatePickerExpanded)) },
                    onTimeExpanded = {
                        onEvent(
                            EventScreenEvent.TimePickerSelected(
                                timePickerSelected = !state.isTimePickerSelectionExpanded
                            )
                        )
                    },
                    alarmTimeTextSelected = { selectedAlarmType ->
                        onEvent(EventScreenEvent.AlarmTimeTextSelected(selectedAlarmType))
                    },
                    alarmTypeDropDownSelected = { alarmSectionExpanded ->
                        onEvent(
                            alarmSectionExpanded
                        )
                    },
                    state = state,
                    enterItemTitle = {
                        onEvent(
                            EventScreenEvent.EnterSetTitleScreen(
                                EditTextScreenType.EDIT_TITLE
                            )
                        )
                    },
                    onTimeSelected = { timePickerState ->
                        onEvent(EventScreenEvent.TimeSelected(selectedTime = timePickerState))
                    },
                    onDateSelected = { selectedDate ->
                        onEvent(selectedDate)
                    },
                    formattedDate = formattedDateForDatePicker,
                    formattedTime = formattedTime
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTimeAndDateRow(
    dateString: String,
    isEventBeingEdited: Boolean,
    onDateExpanded: (Boolean) -> Unit,
    onTimeExpanded: (EventScreenEvent) -> Unit,
    onTimeSelected: (TimePickerState) -> Unit,
    onDateSelected: (Long) -> Unit,
    isDatePickerExpanded: Boolean,
    isTimePickerExpanded: Boolean,
    agendaItemType: AgendaItemType,
    timeString: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 20.dp)
    ) {
        TimeSection(
            isTimePickerExpanded = isTimePickerExpanded,
            selectedTime = timeString,
            toggleTimePicker = {
                onTimeExpanded(EventScreenEvent.TimePickerSelected(!isTimePickerExpanded))
            },
            userSelectedTime = { selectedTime ->
                onTimeSelected(selectedTime)
            },
            isEventBeingEdited = isEventBeingEdited,
            agendaItemType = agendaItemType,
            toOrFromText = "From"
        )
        DateSection(
            isDatePickerExpanded = isDatePickerExpanded,
            datePickerExpanded = { onDateExpanded(isDatePickerExpanded) },
            userSelectedDate = { dateSelected: Long -> onDateSelected(dateSelected) },
            dateString = dateString,
            isEventBeingEdited = isEventBeingEdited,
            agendaItemType = agendaItemType
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreenContent(
    enterItemDescription: (EventScreenEvent) -> Unit,
    enterItemTitle: (EventScreenEvent) -> Unit,
    onTimeExpanded: (Boolean) -> Unit,
    onDateExpanded: (Boolean) -> Unit,
    onTimeSelected: (TimePickerState) -> Unit,
    onDateSelected: (EventScreenEvent) -> Unit,
    alarmTypeDropDownSelected: (EventScreenEvent) -> Unit,
    alarmTimeTextSelected: (ReminderType) -> Unit,
    formattedDate: String,
    formattedTime: String,
    state: EventState
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp, start = 20.dp)
    ) {
        ItemBox("#d0ec44", 30, borderColor = "#d0ec44")
        ItemBoxTitle("Event")
    }
    Column {
        NewItemRow(
            title = state.titleText,
            editTitle = { enterItemTitle(EventScreenEvent.EnterSetTitleScreen(EditTextScreenType.EDIT_TITLE)) },
            isEventBeingEdited = state.isEditingEvent,
            isDone = state.isDone,
            agendaItemType = state.agendaItemType,
            onEvent = { isDone -> (isDone) }
        )
        RowDivider()
        ItemDescription(
            description = state.description,
            enterItemDescription = {
                enterItemDescription(
                    EventScreenEvent.EnterEventDescription(
                        EditTextScreenType.EDIT_DETAILS
                    )
                )
            },
            isEventBeingEdited = state.isEditingEvent
        )
        PictureRow()
        Spacer(modifier = Modifier.height(5.dp))
        RowDivider()
        EventTimeAndDateRow(
            dateString = formattedDate,
            isEventBeingEdited = state.isEditingEvent,
            onDateExpanded = { onDateExpanded(state.isDatePickerExpanded) },
            onTimeExpanded = { onTimeExpanded(state.isTimePickerSelectionExpanded) },
            onTimeSelected = { timePickerState -> onTimeSelected(timePickerState) },
            onDateSelected = { selectedDate ->
                onDateSelected(
                    EventScreenEvent.UpdateDateSelection(
                        selectedDate
                    )
                )
            },
            isDatePickerExpanded = state.isDatePickerExpanded,
            isTimePickerExpanded = state.isTimePickerSelectionExpanded,
            timeString = formattedTime,
            agendaItemType = state.agendaItemType
        )
        RowDivider()
        EndEventTimeAndDateRow(
            dateString = formattedDate,
            isEventBeingEdited = state.isEditingEvent,
            onDateExpanded = { onDateExpanded(state.isDatePickerExpanded) },
            onTimeExpanded = { onTimeExpanded(state.isTimePickerSelectionExpanded) },
            onTimeSelected = { timePickerState -> onTimeSelected(timePickerState) },
            onDateSelected = { selectedDate ->
                onDateSelected(
                    EventScreenEvent.UpdateDateSelection(
                        selectedDate
                    )
                )
            },
            isDatePickerExpanded = state.isDatePickerExpanded,
            isTimePickerExpanded = state.isTimePickerSelectionExpanded,
            timeString = formattedTime,
            agendaItemType = state.agendaItemType
        )
        RowDivider()
        SetAlarmTimeRow(
            isAlarmSelectionExpanded = state.isAlarmSelectionExpanded,
            alarmTimeSelectionText = state.alarmReminderTimeSelection,
            toggleAlarmExpanded = { alarmSectionExpanded ->
                alarmTypeDropDownSelected(
                    EventScreenEvent.AlarmTypeDropDownSelected(alarmSectionExpanded)
                )
            },
            setAlarmType = {
                alarmTimeTextSelected(it)
            },
            isEventBeingEdited = state.isEditingEvent
        )
        RowDivider()
        VisitorSection()
    }
}

@Composable
fun VisitorSection() {
    Row(
        modifier = Modifier
            .padding(top = 25.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row {
                Text(
                    text = "Visitors",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 5.dp)
                )
                AddButton()
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OvalButton(buttonText = "All")
                Spacer(modifier = Modifier.width(5.dp))
                OvalButton(buttonText = "Going")
                Spacer(modifier = Modifier.width(5.dp))
                OvalButton(buttonText = "Not Going")
            }
            Spacer(modifier = Modifier.height(10.dp))
            ListOfAttendees()
        }
    }
}

@Composable
fun ListOfAttendees() {
    Column {
        Text(text = "Going", fontSize = 20.sp)

    }
}

@Composable
fun AddButton() {
    Box(
        modifier = Modifier
            .size(25.dp)
            .background(
                Color(android.graphics.Color.parseColor("#f1f6ff")),
                shape = AbsoluteRoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            )
            .clickable(onClick = {}),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.Black,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun OvalButton(buttonText: String) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(30.dp)
            .background(
                Color(android.graphics.Color.parseColor("#f1f6ff")),
                shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
            )
            .clickable(onClick = {}),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = buttonText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndEventTimeAndDateRow(
    dateString: String,
    isEventBeingEdited: Boolean,
    onDateExpanded: (Boolean) -> Unit,
    onTimeExpanded: (EventScreenEvent) -> Unit,
    onTimeSelected: (TimePickerState) -> Unit,
    onDateSelected: (Long) -> Unit,
    isDatePickerExpanded: Boolean,
    isTimePickerExpanded: Boolean,
    agendaItemType: AgendaItemType,
    timeString: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 20.dp)
    ) {
        TimeSection(
            isTimePickerExpanded = isTimePickerExpanded,
            selectedTime = timeString,
            toggleTimePicker = {
                onTimeExpanded(EventScreenEvent.TimePickerSelected(!isTimePickerExpanded))
            },
            userSelectedTime = { selectedTime ->
                onTimeSelected(selectedTime)
            },
            isEventBeingEdited = isEventBeingEdited,
            agendaItemType = agendaItemType,
            toOrFromText = "To  "
        )
        DateSection(
            isDatePickerExpanded = isDatePickerExpanded,
            datePickerExpanded = { onDateExpanded(isDatePickerExpanded) },
            userSelectedDate = { dateSelected: Long -> onDateSelected(dateSelected) },
            dateString = dateString,
            isEventBeingEdited = isEventBeingEdited,
            agendaItemType = agendaItemType
        )
    }
}

@Composable
fun PictureRow() {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 20.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#f1f6ff"))
            )

    ) {
        Column(
            modifier = Modifier.padding(top = 20.dp, start = 25.dp)

        ) {
            Text(text = "Photos", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                ItemBox(hexColor = "#FFFFFF", size = 75, borderColor = "#c1c7da")
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun SaveEvent(
    saveEvent: (EventScreenEvent.SaveEvent) -> Unit,
    updateEvent: (EventScreenEvent.UpdateEvent) -> Unit,
    title: String,
    description: String,
    isEventBeingEdited: Boolean,
    eventId: String,
) {
    Text(
        text = "SAVE",
        fontSize = 15.sp,
        color = Color.White,
        modifier =
        Modifier
            .clickable {
                if (isEventBeingEdited && eventId == "") {
                    saveEvent(
                        EventScreenEvent.SaveEvent(
                            title,
                            description,
                            null
                        )
                    )
                } else {
                    updateEvent(
                        EventScreenEvent.UpdateEvent(
                            eventId = eventId,
                            title = title,
                            description = description
                        )
                    )
                }
            }
            .padding(end = 10.dp, top = 5.dp)
    )
}