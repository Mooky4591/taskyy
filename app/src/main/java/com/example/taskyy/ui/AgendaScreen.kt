package com.example.taskyy.ui

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
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
import com.example.taskyy.ui.viewmodels.AgendaState
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AgendaScreen(state: AgendaState, onEvent: (AgendaEvent) -> Unit) {
    Scaffold(
        topBar = {
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
                            onEvent(AgendaEvent.OnMonthExpanded(!state.isMonthExpanded))
                        }
                        .padding(vertical = 10.dp)
                ) {
                    DateSelection(
                        isMonthExpanded = state.isMonthExpanded,
                        selectedMonth = state.selectedMonth,
                        onEvent = onEvent
                    )
                }
                CircleWithInitials(
                    isUserDropDownExpanded = state.isUserDropDownExpanded,
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
                )
            }
        },
        content = { PaddingValues ->
            PaddingValues.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr)
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight(.85F)
                        .fillMaxWidth(),
                    shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (state.selectedDay.isNotEmpty()) {
                            for (day in state.selectedDay) {
                                Box(
                                    Modifier.padding(
                                        horizontal = 15.dp,
                                        vertical = 20.dp
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        SelectedDaysOfTheWeek(dayOfTheWeek = day.dayOfTheWeek)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        SelectedDaysOfTheMonth(dayOfTheMonth = day.dayOfTheMonth)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            AddAgendaItem(onEvent = onEvent, isAgendaItemExpanded = state.isAddAgendaItemExpanded)
        },
    )
}

@Composable
fun SelectedDaysOfTheMonth(dayOfTheMonth: String) {
    Text(text = dayOfTheMonth, fontWeight = FontWeight.Bold, fontSize = 15.sp)
}

@RequiresApi(Build.VERSION_CODES.Q)
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
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text(text = "Task") },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text(text = "Reminder") },
                onClick = { /*TODO*/ }
            )
        }
    }
}

@Composable
fun DateSelection(selectedMonth: String, onEvent: (AgendaEvent) -> Unit, isMonthExpanded: Boolean) {
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
                onEvent(AgendaEvent.OnDateSelected(selectedMonth))
            },
        )
    }
}

fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

@Composable
fun CircleWithInitials(isUserDropDownExpanded: Boolean, onUserInitialsClicked: (Boolean) -> Unit, onItemSelected: (AgendaEvent) -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.Gray,
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
                text = "SR",
                fontSize = 16.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.W700,
                color = Color.LightGray
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
fun DropDownMenuItemText(logout: String) {
    Text(text = logout, color = Color.Black)
}

@Composable
fun MonthText(selectedMonth: String){
    Text(text = selectedMonth,
        color = Color.White,
        fontSize = 20.sp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePicker(onItemSelected: (Long) -> Unit){

    val dateTime = LocalDateTime.now()
    val datePickerState = rememberDatePickerState(
        yearRange = (2023..2024),
        initialSelectedDateMillis = dateTime.toMillis(),
        initialDisplayedMonthMillis = dateTime.toMillis(),
        initialDisplayMode = DisplayMode.Picker
    )

    DatePicker(
        state = datePickerState,
        modifier = Modifier.clickable {
            onItemSelected(datePickerState.selectedDateMillis!!)
        },
        colors = DatePickerDefaults.colors(
            titleContentColor = Color.White,
            todayDateBorderColor = Color.Green,
            headlineContentColor = Color.White,
            weekdayContentColor = Color.White,
            dayContentColor = Color.White
        )
    )

}