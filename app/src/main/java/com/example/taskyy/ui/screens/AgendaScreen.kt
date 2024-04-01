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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskyy.R
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.objects.Day
import com.example.taskyy.ui.viewmodels.AgendaState
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun AgendaScreen(state: AgendaState, onEvent: (AgendaEvent) -> Unit) {
    Scaffold(
        content = {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr))
            ) {
                DateSelectionAndUserInitialsButton(
                    isMonthExpanded = state.isMonthExpanded,
                    isUserDropDownExpanded = state.isUserDropDownExpanded,
                    onEvent = onEvent,
                    selectedMonth = state.selectedMonth,
                    initials = state.initials
                )
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.wrapContentHeight(align = Alignment.Top)
                    ) {
                        RowOfDaysToDisplay(
                            selectedDay = state.selectedDayList,
                            selectedIndex = state.selectedIndex,
                            selectedDayIndexOnClick = { index ->
                                onEvent(AgendaEvent.SelectedDayIndex(index = index))
                            },
                            updateDateStringOnSelectedDayClick = { date ->
                                onEvent(AgendaEvent.UpdateDateString(date))
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        Text(
                            text = state.dateString,
                            color = Color.Black,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .height(250.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AddAgendaItem(
                onEvent = onEvent,
                isAgendaItemExpanded = state.isAddAgendaItemExpanded
            )
        },
    )
}

@Composable
fun RowOfDaysToDisplay(
    selectedDay: List<Day>,
    selectedDayIndexOnClick: (Int) -> Unit,
    selectedIndex: Int,
    updateDateStringOnSelectedDayClick: (String) -> Unit
) {
    if (selectedDay.isNotEmpty()) {
        for (day in selectedDay) {
            Surface(
                modifier =
                Modifier
                    .padding(
                        vertical = 20.dp
                    )
                    .size(
                        height = 60.dp,
                        width = 40.dp
                    ),
                shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
                            selectedDayIndexOnClick(day.index)
                            updateDateStringOnSelectedDayClick(day.date)
                        }
                        .background(
                            color =
                            if (day.index == selectedIndex) {
                                Color(android.graphics.Color.parseColor("#feeea8"))
                            } else {
                                Color.Transparent
                            }
                        )
                        .wrapContentSize()
                ) {
                    SelectedDaysOfTheWeek(dayOfTheWeek = day.dayOfTheWeek)
                    Spacer(modifier = Modifier.height(10.dp))
                    SelectedDaysOfTheMonth(dayOfTheMonth = day.dayOfTheMonth)
                }
            }
        }
    }
}

@Composable
fun DateSelectionAndUserInitialsButton(
    isMonthExpanded: Boolean,
    onEvent: (AgendaEvent) -> Unit,
    selectedMonth: String,
    isUserDropDownExpanded: Boolean,
    initials: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onEvent(AgendaEvent.OnMonthExpanded(!isMonthExpanded))
                }
                .padding(top = 10.dp)
        ) {
            DateSelection(
                isMonthExpanded = isMonthExpanded,
                selectedMonth = selectedMonth,
                datePickerExpanded = { onEvent(AgendaEvent.OnMonthExpanded(isMonthExpanded)) },
                userSelectedDate = { userSelectedDate: Long ->
                    onEvent(
                        AgendaEvent.OnDateSelected(
                            userSelectedDate
                        )
                    )
                }
            )
        }
        CircleWithInitials(
            isUserDropDownExpanded = isUserDropDownExpanded,
            onUserInitialsClicked = { isUserDropDownExpanded ->
                onEvent(
                    AgendaEvent.OnUserInitialsClicked(
                        isUserDropDownExpanded
                    )
                )
            },
            onItemSelected = {
                onEvent(AgendaEvent.OnLogOutCLicked)
            },
            initials = initials
        )
    }
}

@Composable
fun SelectedDaysOfTheMonth(dayOfTheMonth: String) {
    Text(text = dayOfTheMonth, fontWeight = FontWeight.Bold, fontSize = 15.sp)
}

@Composable
fun SelectedDaysOfTheWeek(dayOfTheWeek: String) {
    if (dayOfTheWeek != "") {
        Text(
            text = dayOfTheWeek.substring(0, 1),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color.LightGray
        )
    }
}

@Composable
fun AddAgendaItem(onEvent: (AgendaEvent) -> Unit, isAgendaItemExpanded: Boolean) {
    FloatingActionButton(
        onClick = {
            onEvent(AgendaEvent.AddAgendaItem(isAgendaItemExpanded = !isAgendaItemExpanded))
        },
        shape = AbsoluteRoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        containerColor = Color.Black,
        contentColor = Color.White,
    ) {
        Text(text = "+", fontSize = 25.sp)
        DropdownMenu(
            expanded = isAgendaItemExpanded,
            onDismissRequest = {
                onEvent(AgendaEvent.AddAgendaItem(!isAgendaItemExpanded))
            },
            modifier = Modifier.background(Color.White),
        ) {
            DropdownMenuItem(
                text = { Text(text = "Event") },
                onClick = {
                    onEvent(AgendaEvent.EventItemSelected)
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Task") },
                onClick = {
                    onEvent(AgendaEvent.TaskItemSelected)
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Reminder") },
                onClick = {
                    onEvent(AgendaEvent.ReminderItemSelected)
                }
            )
        }
    }
}

@Composable
fun DateSelection(
    selectedMonth: String,
    datePickerExpanded: (AgendaEvent) -> Unit,
    userSelectedDate: (Long) -> Unit,
    isMonthExpanded: Boolean
) {
    if (selectedMonth == "") {
        MonthText(selectedMonth = LocalDateTime.now().month.toString())
    } else {
        MonthText(selectedMonth = selectedMonth)
    }
    Icon(
        painter = painterResource(id = R.drawable.drop_down_arrow),
        contentDescription = "", tint = Color.White
    )
    if (isMonthExpanded) {
        ShowDatePicker(
            onItemSelected = { selectedMonth ->
                userSelectedDate(selectedMonth)
            },
            cancelled = { isMonthExpanded ->
                datePickerExpanded(AgendaEvent.OnMonthExpanded(isMonthExpanded))
            },
            isDatePickerExpanded = isMonthExpanded
        )
    }
}

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

@Composable
fun CircleWithInitials(
    isUserDropDownExpanded: Boolean,
    onUserInitialsClicked: (Boolean) -> Unit,
    onItemSelected: (AgendaEvent) -> Unit,
    initials: String
) {
    Surface(
        shape = CircleShape,
        color = Color.LightGray,
        modifier =
        Modifier
            .size(40.dp)
            .clickable {
                onUserInitialsClicked(!isUserDropDownExpanded)
            },
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initials,
                fontSize = 16.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.W700,
                color = Color.Gray
            )
        }

        DropdownMenu(
            expanded = isUserDropDownExpanded,
            onDismissRequest = {
                onUserInitialsClicked(!isUserDropDownExpanded)
            },
            modifier = Modifier.background(Color.White),
        ) {
            DropdownMenuItem(
                text = {
                    DropDownMenuItemText(stringResource(R.string.logout))
                },
                onClick = {
                    onItemSelected(AgendaEvent.OnLogOutCLicked)
                },
            )
        }
    }
}

@Composable
fun DropDownMenuItemText(itemTitle: String) {
    Text(text = itemTitle, color = Color.Black)
}

@Composable
fun MonthText(selectedMonth: String) {
    Text(
        text = selectedMonth,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePicker(
    onItemSelected: (Long) -> Unit,
    cancelled: (Boolean) -> Unit,
    isDatePickerExpanded: Boolean
) {

    val dateTime = LocalDateTime.now()
    val datePickerState = rememberDatePickerState(
        yearRange = (2023..2024),
        initialSelectedDateMillis = dateTime.toMillis(),
        initialDisplayedMonthMillis = dateTime.toMillis(),
        initialDisplayMode = DisplayMode.Picker
    )

    DatePickerDialog(
        onDismissRequest = {
            cancelled(!isDatePickerExpanded)
        },
        confirmButton = {
            Text(
                text = "Select",
                modifier = Modifier
                    .clickable {
                        onItemSelected(datePickerState.selectedDateMillis!!)
                        cancelled(!isDatePickerExpanded)
                    }
                    .padding(end = 15.dp, bottom = 15.dp),
            )
        },
        content = {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = Color.White,
                    todayDateBorderColor = Color.Green,
                    headlineContentColor = Color.White,
                    weekdayContentColor = Color.White,
                    dayContentColor = Color.White,
                    navigationContentColor = Color.White
                )
            )
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color.Black
        )
    )
}