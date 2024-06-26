package com.example.taskyy.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.taskyy.R
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.enums.ReminderType
import com.example.taskyy.ui.events.EditTextEvent
import com.example.taskyy.ui.events.EventScreenEvent
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.viewmodels.ReminderState
import com.example.taskyy.ui.viewmodels.TimeAndDateState
import java.time.format.DateTimeFormatter

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    state: ReminderState,
    onEvent: (ReminderEvent) -> Unit,
    timeDateState: TimeAndDateState
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
                    SaveReminder(
                        saveEvent = { reminder ->
                            onEvent(reminder)
                        },
                        updateEvent = { reminder ->
                            onEvent(reminder)
                        },
                        title = state.reminderTitleText,
                        description = state.reminderDescription,
                        isEventBeingEdited = state.isEditingEvent,
                        eventId = state.eventId,
                        agendaItemType = state.agendaItemType,
                        isDone = state.isDone
                    )
                },
                onClose = { (onEvent(ReminderEvent.Close)) }
            )
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
            ) {
                ReminderScreenContent(
                    enterItemDescription = {
                        onEvent(
                            ReminderEvent.EnterReminderDescription(
                                EditTextScreenType.EDIT_DETAILS
                            )
                        )
                    },
                    onDateExpanded = { onEvent(ReminderEvent.DatePickerSelcted(datePickerExpanded = !state.isDatePickerExpanded)) },
                    markTaskDone = { isDone ->
                        onEvent(ReminderEvent.MarkTaskDone(isDone))
                    },
                    onTimeExpanded = { onEvent(ReminderEvent.TimePickerSelected(timePickerSelected = !state.isTimePickerSelectionExpanded)) },
                    alarmTimeTextSelected = { selectedAlarmType ->
                        onEvent(ReminderEvent.AlarmTimeTextSelected(selectedAlarmType))
                    },
                    alarmTypeDropDownSelected = { alarmSectionExpanded ->
                        onEvent(
                            alarmSectionExpanded
                        )
                    },
                    state = state,
                    enterItemTitle = { onEvent(ReminderEvent.EnterSetTitleScreen(EditTextScreenType.EDIT_TITLE)) },
                    onTimeSelected = { timePickerState ->
                        onEvent(ReminderEvent.TimeSelected(selectedTime = timePickerState))
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
private fun ReminderScreenContent(
    enterItemDescription: (ReminderEvent) -> Unit,
    enterItemTitle: (ReminderEvent) -> Unit,
    onTimeExpanded: (Boolean) -> Unit,
    onDateExpanded: (Boolean) -> Unit,
    onTimeSelected: (TimePickerState) -> Unit,
    onDateSelected: (ReminderEvent) -> Unit,
    alarmTypeDropDownSelected: (ReminderEvent) -> Unit,
    alarmTimeTextSelected: (ReminderType) -> Unit,
    markTaskDone: (Boolean) -> Unit,
    formattedDate: String,
    formattedTime: String,
    state: ReminderState
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp, start = 20.dp)
    ) {
        when (state.agendaItemType) {
            AgendaItemType.REMINDER_ITEM -> {
                ItemBox("#f2f6ff", 30, borderColor = "#d0d2d9")
                ItemBoxTitle("Reminder")
            }

            AgendaItemType.TASK_ITEM -> {
                ItemBox("#289c74", 30, borderColor = "#289c74")
                ItemBoxTitle("Task")
            }
            else -> {}
        }
    }
    Column {
        NewItemRow(
            title = state.reminderTitleText,
            editTitle =
            { enterItemTitle(ReminderEvent.EnterSetTitleScreen(EditTextScreenType.EDIT_TITLE)) },
            isEventBeingEdited = state.isEditingEvent,
            isDone = state.isDone,
            agendaItemType = state.agendaItemType,
            onEvent = { isDone -> markTaskDone(isDone) }
        )
        RowDivider()
        ItemDescription(
            description = state.reminderDescription,
            enterItemDescription = {
                enterItemDescription(
                    ReminderEvent.EnterReminderDescription(
                        EditTextScreenType.EDIT_DETAILS
                    )
                )
            },
            isEventBeingEdited = state.isEditingEvent
        )
        RowDivider()
        TimeAndDateRow(
            dateString = formattedDate,
            isEventBeingEdited = state.isEditingEvent,
            isDatePickerExpanded = state.isDatePickerExpanded,
            timeString = formattedTime,
            isTimePickerExpanded = state.isTimePickerSelectionExpanded,
            onDateExpanded = { onDateExpanded(state.isDatePickerExpanded) },
            onTimeExpanded = { onTimeExpanded(state.isTimePickerSelectionExpanded) },
            onTimeSelected = { timePickerState -> onTimeSelected(timePickerState) },
            onDateSelected = { selectedDate ->
                onDateSelected(
                    ReminderEvent.UpdateDateSelection(
                        selectedDate
                    )
                )
            },
            agendaItemType = state.agendaItemType
        )
        RowDivider()
        SetAlarmTimeRow(
            isAlarmSelectionExpanded = state.isAlarmSelectionExpanded,
            alarmTimeSelectionText = state.alarmReminderTimeSelection,
            toggleAlarmExpanded = { alarmSectionExpanded ->
                alarmTypeDropDownSelected(
                    ReminderEvent.AlarmTypeDropDownSelected(alarmSectionExpanded)
                )
            },
            setAlarmType = {
                alarmTimeTextSelected(it)
            },
            isEventBeingEdited = state.isEditingEvent
        )
        RowDivider()
    }
}

@Composable
fun DropDownItemSetUp(
    toggleAlarmExpanded: (Boolean) -> Unit,
    isAlarmSelectionExpanded: Boolean,
    setAlarmType: (ReminderType) -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(text = "10 minutes before")
        },
        onClick = {
            toggleAlarmExpanded(!isAlarmSelectionExpanded)
            setAlarmType(ReminderType.TEN_MINUTES_BEFORE)
        }
    )

    DropdownMenuItem(
        text = {
            Text(text = "30 minutes before")
        },
        onClick = {
            toggleAlarmExpanded(!isAlarmSelectionExpanded)
            setAlarmType(ReminderType.THIRTY_MINUTES_BEFORE)
        }
    )
    DropdownMenuItem(
        text = {
            Text(text = "1 hour before")
        },
        onClick = {
            toggleAlarmExpanded(!isAlarmSelectionExpanded)
            setAlarmType(ReminderType.ONE_HOUR_BEFORE)
        }
    )
    DropdownMenuItem(
        text = {
            Text(text = "6 hours before")
        },
        onClick = {
            toggleAlarmExpanded(!isAlarmSelectionExpanded)
            setAlarmType(ReminderType.SIX_HOURS_BEFORE)
        }
    )
    DropdownMenuItem(
        text = {
            Text(text = "1 day before")
        },
        onClick = {
            toggleAlarmExpanded(!isAlarmSelectionExpanded)
            setAlarmType(ReminderType.ONE_DAY_BEFORE)
        }
    )
}

@Composable
fun SetAlarmTimeRow(
    isAlarmSelectionExpanded: Boolean,
    alarmTimeSelectionText: String,
    isEventBeingEdited: Boolean,
    toggleAlarmExpanded: (Boolean) -> Unit,
    setAlarmType: (ReminderType) -> Unit
) {
    Row(
        modifier =
        Modifier
            .padding(start = 20.dp)
            .clickable(enabled = isEventBeingEdited) {
                toggleAlarmExpanded(!isAlarmSelectionExpanded)
            }
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AlarmIconWithBackground()
        val alarmText =
            if (alarmTimeSelectionText == "") {
                "1 hour before"
            } else {
                alarmTimeSelectionText
            }
        Text(
            text = alarmText,
            color = Color.Black,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
        DropdownMenu(
            expanded = isAlarmSelectionExpanded,
            onDismissRequest = {
                toggleAlarmExpanded(!isAlarmSelectionExpanded)
            }
        ) {
            DropDownItemSetUp(
                toggleAlarmExpanded = toggleAlarmExpanded,
                isAlarmSelectionExpanded = isAlarmSelectionExpanded,
                setAlarmType = setAlarmType
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ChevronForward(isEventBeingEdited)
        }
    }
}

@Composable
fun DateSection(
    datePickerExpanded: (Any) -> Unit,
    dateString: String,
    isDatePickerExpanded: Boolean,
    isEventBeingEdited: Boolean,
    agendaItemType: AgendaItemType,
    userSelectedDate: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(enabled = isEventBeingEdited) {
                if (agendaItemType == AgendaItemType.TASK_ITEM || agendaItemType == AgendaItemType.REMINDER_ITEM) {
                    datePickerExpanded(ReminderEvent.DatePickerSelcted(!isDatePickerExpanded))
                } else {
                    datePickerExpanded(EventScreenEvent.DatePickerSelcted(!isDatePickerExpanded))
                }
            }
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dateString,
            color = Color.Black,
            fontSize = 15.sp
        )
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ChevronForward(isEventBeingEdited)
        }
    }
    if (isDatePickerExpanded) {
        ShowDatePicker(
            onItemSelected = {
                userSelectedDate(it)
            },
            cancelled = { isDatePickerExpanded ->
                if (agendaItemType == AgendaItemType.TASK_ITEM || agendaItemType == AgendaItemType.REMINDER_ITEM) {
                    datePickerExpanded(ReminderEvent.DatePickerSelcted(isDatePickerExpanded))
                } else {
                    datePickerExpanded(EventScreenEvent.DatePickerSelcted(isDatePickerExpanded))
                }
            },
            isDatePickerExpanded = isDatePickerExpanded
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeAndDateRow(
    dateString: String,
    isEventBeingEdited: Boolean,
    onDateExpanded: (Boolean) -> Unit,
    onTimeExpanded: (ReminderEvent) -> Unit,
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
                onTimeExpanded(ReminderEvent.TimePickerSelected(!isTimePickerExpanded))
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
fun TimeSection(
    toggleTimePicker: (Any) -> Unit,
    userSelectedTime: (TimePickerState) -> Unit,
    agendaItemType: AgendaItemType,
    isTimePickerExpanded: Boolean,
    isEventBeingEdited: Boolean,
    selectedTime: String,
    toOrFromText: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(enabled = isEventBeingEdited) {
                if (agendaItemType == AgendaItemType.REMINDER_ITEM || agendaItemType == AgendaItemType.TASK_ITEM) {
                    toggleTimePicker(ReminderEvent.TimePickerSelected(!isTimePickerExpanded))
                } else {
                    toggleTimePicker(EventScreenEvent.TimePickerSelected(!isTimePickerExpanded))
                }
            }
            .height(40.dp)
    ) {
        Text(
            text = toOrFromText,
            modifier = Modifier.padding(end = 40.dp),
            fontSize = 15.sp
        )
        Text(
            text = selectedTime,
            modifier = Modifier.padding(end = 40.dp),
            fontSize = 15.sp
        )
        if (isEventBeingEdited) {
            Image(
                painter = painterResource(id = R.drawable.chevron_forward),
                contentDescription = "proceed forward arrow",
                modifier = Modifier.padding(end = 40.dp)
            )
        }
    }
    if (isTimePickerExpanded) {
        TimePickerDialog(
            timeSelected = { selectedTime ->
                userSelectedTime(selectedTime)
            },
            toggle = {
                if (agendaItemType == AgendaItemType.REMINDER_ITEM || agendaItemType == AgendaItemType.TASK_ITEM) {
                    toggleTimePicker(ReminderEvent.TimePickerSelected(!it))
                } else {
                    toggleTimePicker(EventScreenEvent.TimePickerSelected(!it))
                }
            },
            isTimeSelectionExpanded = isTimePickerExpanded
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    timeSelected: (TimePickerState) -> Unit,
    toggle: (Boolean) -> Unit,
    isTimeSelectionExpanded: Boolean
) {
    val pickedTime = rememberTimePickerState()

    Dialog(
        onDismissRequest = {
            toggle(!isTimeSelectionExpanded)
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),

        ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = Color.Black
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = "Select Time",
                    style = MaterialTheme.typography.labelMedium
                )
                TimePicker(
                    state = pickedTime,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color.White,
                        containerColor = Color.Black,
                    )
                )
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            toggle(!isTimeSelectionExpanded)
                        }
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = {
                            timeSelected(pickedTime)
                            toggle(!isTimeSelectionExpanded)
                        }
                    ) { Text("OK") }
                }
            }
        }
    }
}


@Composable
fun ItemDescription(
    description: String,
    enterItemDescription: (ReminderEvent) -> Unit,
    isEventBeingEdited: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(enabled = isEventBeingEdited)
            {
                enterItemDescription(ReminderEvent.EnterReminderDescription(EditTextScreenType.EDIT_DETAILS))
            }
            .height(40.dp)
    ) {
        Text(
            text = description,
            modifier = Modifier.padding(start = 20.dp),
            fontSize = 15.sp
        )
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ChevronForward(isEventBeingEdited)
        }
    }
}

@Composable
fun RowDivider() {
    HorizontalDivider(
        color = Color(android.graphics.Color.parseColor("#f8f8fa")),
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 4.dp
            )
    )
}

@Composable
fun NewItemRow(
    title: String,
    editTitle: (String) -> Unit,
    isEventBeingEdited: Boolean,
    isDone: Boolean,
    agendaItemType: AgendaItemType,
    onEvent: (Boolean) -> Unit
) {
    Row(
        modifier =
        Modifier
            .padding(top = 60.dp, start = 20.dp)
            .clickable(enabled = isEventBeingEdited) {
                editTitle(title)
            }
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleWithBlackBorder(color = "#FFFFFF")
        Text(
            text = title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ChevronForward(isEventBeingEdited = isEventBeingEdited)
        }
    }
}

@Composable
fun ChevronForward(isEventBeingEdited: Boolean) {
    if (isEventBeingEdited) {
        Image(
            painter = painterResource(id = R.drawable.chevron_forward),
            contentDescription = "proceed forward arrow",
            modifier = Modifier.padding(end = 15.dp)
        )
    }
}

@Composable
fun AlarmIconWithBackground() {
    Box(
        modifier = Modifier
            .background(
                color = Color(android.graphics.Color.parseColor("#f2f6fc")),
                shape = AbsoluteRoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            ),
        contentAlignment = Alignment.Center

    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.alarm_icon),
            tint = Color(0xFFAEB4BF),
            contentDescription = "Alarm icon",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun CircleWithBlackBorder(color: String) {
    Box(
        modifier = Modifier
            .size(20.dp) // Adjust the size of the circle
            .background(
                color = Color(android.graphics.Color.parseColor(color)), shape = CircleShape
            )
            .border(width = 2.dp, color = Color.Black, shape = CircleShape)

    )
}

@Composable
fun ItemBoxTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        color = Color.Black,
        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
    )
}

@Composable
fun ItemBox(hexColor: String, size: Int, borderColor: String) {
    Box(
        modifier =
        Modifier
            .background(
                color = Color(android.graphics.Color.parseColor(hexColor)),
                shape = AbsoluteRoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            )
            .border(
                width = 2.dp,
                color = Color(android.graphics.Color.parseColor(borderColor)),
                shape = AbsoluteRoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            )
            .size(size.dp)
    )
}

@Composable
fun TopBar(
    title: String,
    onClose: () -> Unit,
    color: Color,
    saveFunction: @Composable () -> Unit
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.close
            ),
            contentDescription = "",
            tint = color,
            modifier =
            Modifier.clickable {
                onClose()
            }

        )
        Text(
            text = title,
            fontSize = 25.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
        saveFunction()
    }
}

@Composable
fun SaveReminder(
    saveEvent: (ReminderEvent.SaveReminder) -> Unit,
    updateEvent: (ReminderEvent.UpdateReminder) -> Unit,
    title: String,
    description: String,
    isEventBeingEdited: Boolean,
    eventId: String,
    isDone: Boolean,
    agendaItemType: AgendaItemType
) {
    Text(
        text = "SAVE",
        fontSize = 15.sp,
        color = Color.White,
        modifier =
        Modifier
            .clickable {
                if (isEventBeingEdited && eventId == "") {
                    if (agendaItemType == AgendaItemType.REMINDER_ITEM) {
                        saveEvent(
                            ReminderEvent.SaveReminder(
                                title = title,
                                description = description,
                                isDone = null
                            )
                        )
                    } else if (agendaItemType == AgendaItemType.TASK_ITEM) {
                        saveEvent(
                            ReminderEvent.SaveReminder(
                                title = title,
                                description = description,
                                isDone = isDone
                            )
                        )
                    }
                } else {
                    updateEvent(
                        ReminderEvent.UpdateReminder(
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

@Composable
fun SaveDetails(onEvent: (EditTextEvent) -> Unit, details: String) {
    Text(
        text = "SAVE",
        fontSize = 15.sp,
        color = Color(android.graphics.Color.parseColor("#7db4a7")),
        modifier =
        Modifier
            .clickable {
                onEvent(EditTextEvent.SaveDescription(details))
            }
            .padding(end = 10.dp, top = 5.dp)
    )
}

@Composable
fun SaveTitle(details: String, onEvent: (EditTextEvent) -> Unit) {
    Text(
        text = "SAVE",
        fontSize = 15.sp,
        color = Color(android.graphics.Color.parseColor("#7db4a7")),
        modifier =
        Modifier
            .clickable {
                onEvent(EditTextEvent.SaveTitle(details))
            }
            .padding(end = 10.dp, top = 5.dp)
    )
}

@Composable
fun CheckForNotificationPermissions() {
    val context = LocalContext.current
    var hasNotificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasNotificationPermission = isGranted
                // TODO: Handle permission denial later
            },
        )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
        SideEffect {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}