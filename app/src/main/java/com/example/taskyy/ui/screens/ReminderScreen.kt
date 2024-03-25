package com.example.taskyy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.taskyy.R
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.viewmodels.ReminderState

@Composable
fun ReminderScreen(state: ReminderState, onEvent: (ReminderEvent) -> Unit) {
    Scaffold(
        content = {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr))
            ) {
                TopBar(dateString = state.dateString, onEvent = onEvent)
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
                ) {
                    ReminderScreenContent(
                        dateString = state.dateString,
                        onEvent = onEvent,
                        state = state
                    )
                }
            }
        }
    )
}

@Composable
private fun ReminderScreenContent(
    dateString: String,
    onEvent: (ReminderEvent) -> Unit,
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
        NewItemRow(title = state.reminderTitleText)
        RowDivider()
        ItemDescription(description = state.reminderDescription)
        RowDivider()
        TimeAndDateRow(
            dateString = dateString,
            onEvent = onEvent,
            isDatePickerExpanded = state.isDatePickerExpanded,
            selectedTime = state.selectedTime,
            selectedDate = state.selectedDate
        )
        RowDivider()
        SetAlarmTimeRow(
            isAlarmSelectionExpanded = state.isAlarmSelectionExpanded,
            alarmTimeSelectionText = state.alarmReminderTimeSelection,
            onEvent = { alarmSectionExpanded ->
                onEvent(
                    ReminderEvent.AlarmTypeDropDownSelected(alarmSectionExpanded)
                )
            })
    }
}

@Composable
fun SetAlarmTimeRow(
    isAlarmSelectionExpanded: Boolean,
    alarmTimeSelectionText: String,
    onEvent: (Boolean) -> Unit
) {
    Row(
        modifier =
        Modifier
            .padding(start = 20.dp)
            .clickable {
                onEvent(!isAlarmSelectionExpanded)
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
                onEvent(!isAlarmSelectionExpanded)
            }
        ) {
            DropdownMenuItem(
                text = {
                    /*TODO*/
                },
                onClick = {
                    /*TODO*/
                }
            )

            DropdownMenuItem(
                text = {
                    /*TODO*/
                },
                onClick = {
                    /*TODO*/
                }
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
fun TimeAndDateRow(
    dateString: String,
    onEvent: (ReminderEvent) -> Unit,
    isDatePickerExpanded: Boolean,
    selectedTime: String,
    selectedDate: Long
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
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
fun ShowTimePicker() {
    TODO("Not yet implemented")
}

@Composable
fun ItemDescription(description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable
            {
                //navigate to item description page
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
fun NewItemRow(title: String) {
    Row(
        modifier =
        Modifier
            .padding(top = 60.dp, start = 20.dp)
            .clickable {
                //navigate to name screen
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
fun TopBar(dateString: String, onEvent: (ReminderEvent) -> Unit) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LaunchedEffect(Unit) {
            onEvent(ReminderEvent.SetDateString)
        }
        Text(
            text = "X",
            fontSize = 20.sp,
            color = Color.White,
            modifier =
            Modifier
                .clickable {
                    onEvent(ReminderEvent.Close)
                    //navigate to backstack
                }
                .padding(start = 10.dp)
        )
        Text(
            text = dateString,
            fontSize = 25.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "SAVE",
            fontSize = 15.sp,
            color = Color.White,
            modifier =
            Modifier
                .clickable {
                    onEvent(ReminderEvent.SaveSelected)
                }
                .padding(end = 10.dp, top = 5.dp)
        )
    }
}
