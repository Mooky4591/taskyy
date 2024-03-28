package com.example.taskyy.ui.screens

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
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.taskyy.R
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.enums.ReminderType
import com.example.taskyy.ui.events.EditTextEvent
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.viewmodels.ReminderState

@Composable
fun ReminderScreen(state: ReminderState, onEvent: (ReminderEvent) -> Unit) {
    Scaffold {
        onEvent(ReminderEvent.SetUserDefaults)
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr))
        ) {
            TopBar(
                title = state.dateString,
                color = Color.White,
                saveFunction = {
                    SaveReminder(onEvent = { onEvent(ReminderEvent.SaveReminder) })
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
                    onDateSelected = { onEvent(ReminderEvent.DatePickerSelcted(datePickerExpanded = state.isDatePickerExpanded)) },
                    onTimeSelected = { onEvent(ReminderEvent.TimePickerSelected(timePickerSelected = state.isTimePickerSelectionExpanded)) },
                    alarmTimeTextSelected = { selectedAlarmType ->
                        onEvent(ReminderEvent.AlarmTimeTextSelected(selectedAlarmType))
                    },
                    alarmTypeDropDownSelected = { alarmSectionExpanded ->
                        onEvent(
                            alarmSectionExpanded
                        )
                    },
                    state = state,
                    enterItemTitle = { onEvent(ReminderEvent.EnterSetTitleScreen(EditTextScreenType.EDIT_TITLE)) }
                )
            }
        }
    }
}

@Composable
private fun ReminderScreenContent(
    enterItemDescription: (ReminderEvent) -> Unit,
    enterItemTitle: (ReminderEvent) -> Unit,
    onTimeSelected: (ReminderEvent) -> Unit,
    onDateSelected: (ReminderEvent) -> Unit,
    alarmTypeDropDownSelected: (ReminderEvent) -> Unit,
    alarmTimeTextSelected: (ReminderType) -> Unit,
    state: ReminderState
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp, start = 20.dp)
    ) {
        ItemBox("#f2f6ff", 30, borderColor = "#d0d2d9")
        ItemBoxTitle("Reminder")
    }
    Column {
        NewItemRow(title = state.reminderTitleText,
            editTitle =
            { enterItemTitle(ReminderEvent.EnterSetTitleScreen(EditTextScreenType.EDIT_TITLE)) }
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
            })
        RowDivider()
        TimeAndDateRow(
            dateString = state.dateString,
            onTimeSelected = { onTimeSelected(ReminderEvent.TimePickerSelected(timePickerSelected = state.isTimePickerSelectionExpanded)) },
            isDatePickerExpanded = state.isDatePickerExpanded,
            selectedTime = state.selectedTime,
            isTimePickerExpanded = state.isTimePickerSelectionExpanded,
            onDateSelected = { onDateSelected(ReminderEvent.DatePickerSelcted(datePickerExpanded = state.isDatePickerExpanded)) }
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
            }
        )
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
    toggleAlarmExpanded: (Boolean) -> Unit,
    setAlarmType: (ReminderType) -> Unit
) {
    Row(
        modifier =
        Modifier
            .padding(start = 20.dp)
            .clickable {
                toggleAlarmExpanded(!isAlarmSelectionExpanded)
            }
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AlarmIconWithBackground()
        Text(
            text = alarmTimeSelectionText,
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
            ChevronForward()
        }
    }
}

@Composable
fun DateSection(
    onEvent: (ReminderEvent) -> Unit,
    dateString: String,
    isDatePickerExpanded: Boolean
) {
    Row(
        modifier = Modifier
            .clickable {
                onEvent(ReminderEvent.DatePickerSelcted(!isDatePickerExpanded))
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
            ChevronForward()
        }
    }
    if (isDatePickerExpanded) {
        ShowDatePicker(
            onItemSelected = { selectedDate ->
                onEvent(ReminderEvent.UpdateDateSelection(selectedDate))
            },
            cancelled = { isDatePickerExpanded ->
                onEvent(ReminderEvent.DatePickerSelcted(isDatePickerExpanded))
            },
            isDatePickerExpanded = isDatePickerExpanded
        )
    }
}

@Composable
fun TimeAndDateRow(
    dateString: String,
    onTimeSelected: (Any?) -> Unit,
    onDateSelected: (Any?) -> Unit,
    isDatePickerExpanded: Boolean,
    isTimePickerExpanded: Boolean,
    selectedTime: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 20.dp)
    ) {
        TimeSection(
            isTimePickerExpanded = isTimePickerExpanded,
            onEvent = onTimeSelected,
            selectedTime = selectedTime
        )
        DateSection(
            isDatePickerExpanded = isDatePickerExpanded,
            onEvent = onDateSelected,
            dateString = dateString
        )
    }
}

@Composable
fun TimeSection(
    onEvent: (Any) -> Unit,
    isTimePickerExpanded: Boolean,
    selectedTime: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onEvent(ReminderEvent.TimePickerSelected(!isTimePickerExpanded))
            }
            .height(40.dp)
    ) {
        Text(
            text = "From",
            modifier = Modifier.padding(end = 40.dp),
            fontSize = 15.sp
        )
        Text(
            text = selectedTime,
            modifier = Modifier.padding(end = 40.dp),
            fontSize = 15.sp
        )
        Image(
            painter = painterResource(id = R.drawable.chevron_forward),
            contentDescription = "proceed forward arrow",
            modifier = Modifier.padding(end = 40.dp)
        )
    }
    if (isTimePickerExpanded) {
        TimePickerDialog(
            timeSelected = { selectedTime ->
                onEvent(ReminderEvent.TimeSelected(selectedTime = selectedTime))
            },
            toggle = {
                onEvent(ReminderEvent.TimePickerSelected(it))
            },
            isTimeSelectionExpanded = isTimePickerExpanded
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    timeSelected: (String) -> Unit,
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
                            timeSelected("${pickedTime.hour}:${pickedTime.minute}")
                            toggle(!isTimeSelectionExpanded)
                        }
                    ) { Text("OK") }
                }
            }
        }
    }
}


@Composable
fun ItemDescription(description: String, enterItemDescription: (ReminderEvent) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable
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
            ChevronForward()
        }
    }
}

@Composable
fun RowDivider() {
    HorizontalDivider(
        color = Color.LightGray,
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 4.dp
            )
    )
}

@Composable
fun NewItemRow(title: String, editTitle: (String) -> Unit) {
    Row(
        modifier =
        Modifier
            .padding(top = 60.dp, start = 20.dp)
            .clickable {
                editTitle(title)
            }
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WhiteCircleWithBlackBorder()
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
            ChevronForward()
        }
    }
}

@Composable
fun ChevronForward() {
    Image(
        painter = painterResource(id = R.drawable.chevron_forward),
        contentDescription = "proceed forward arrow",
        modifier = Modifier.padding(end = 15.dp)
    )
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
fun WhiteCircleWithBlackBorder() {
    Box(
        modifier = Modifier
            .size(20.dp) // Adjust the size of the circle
            .background(color = Color.White, shape = CircleShape)
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
            .padding(top = 10.dp, bottom = 10.dp),
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
fun SaveReminder(onEvent: (ReminderEvent.SaveReminder) -> Unit) {
    Text(
        text = "SAVE",
        fontSize = 15.sp,
        color = Color.White,
        modifier =
        Modifier
            .clickable {
                onEvent(ReminderEvent.SaveReminder)
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
                onEvent(EditTextEvent.TextUpdated(details))
                onEvent(EditTextEvent.SaveDescription(details))
                onEvent(EditTextEvent.Back)
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
                onEvent(EditTextEvent.TextUpdated(details))
                onEvent(EditTextEvent.SaveTitle(details))
                onEvent(EditTextEvent.Back)
            }
            .padding(end = 10.dp, top = 5.dp)
    )
}

