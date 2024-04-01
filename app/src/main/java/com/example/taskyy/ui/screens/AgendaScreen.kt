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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskyy.R
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.objects.AgendaEventItem
import com.example.taskyy.ui.objects.Day
import com.example.taskyy.ui.viewmodels.AgendaState
import com.example.taskyy.ui.viewmodels.TimeDateState
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AgendaScreen(state: AgendaState, onEvent: (AgendaEvent) -> Unit, timeDateState: TimeDateState) {
    Scaffold(
        content = {
            val formattedTitleDate = remember(timeDateState.dateTime) {
                DateTimeFormatter.ofPattern("MMMM dd, yyyy").format(timeDateState.dateTime)
            }
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
                                onEvent(AgendaEvent.OnDateSelected(date))
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        Text(
                            text = formattedTitleDate,
                            color = Color.Black,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .height(250.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                        ListOfAgendaEvents(state.listOfAgendaEvents)
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
fun ListOfAgendaEvents(reminders: List<AgendaEventItem>) {
    LazyColumn {
        items(items = reminders, key = { item -> item.title }) { item ->
            AgendaDisplayItem(
                title = item.title,
                description = item.description,
                date = item.alarmType.toString(),
                onEllipsisClick = { /*TODO*/ })
        }
    }
}

@Preview
@Composable
fun AgendaScreenPreview() {
    // Sample data for preview
    val state = AgendaState(
        isMonthExpanded = false,
        isUserDropDownExpanded = false,
        selectedMonth = "March 2024",
        initials = "JD",
        selectedDayList = listOf(
            Day("Monday", "1", 0, 1704102400000),
            Day("Tuesday", "2", 1, 1704188800000),
            Day("Wednesday", "3", 2, 1704275200000),
            Day("Thursday", "4", 3, 1704361600000),
            Day("Friday", "5", 4, 1704448000000),
            Day("Saturday", "6", 5, 1704534400000),
        ),
        selectedIndex = 0,
        isAddAgendaItemExpanded = false
    )
    val timeDateState = TimeDateState(
        dateTime = LocalDateTime.now()
    )

    // Preview the AgendaScreen composable with sample data
    AgendaScreen(state = state, onEvent = {}, timeDateState = timeDateState)
}



@Composable
fun RowOfDaysToDisplay(
    selectedDay: List<Day>,
    selectedDayIndexOnClick: (Int) -> Unit,
    selectedIndex: Int,
    updateDateStringOnSelectedDayClick: (Long) -> Unit
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

@Composable
fun AgendaDisplayItem(
    title: String,
    description: String,
    date: String,
    onEllipsisClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { TODO() }
            .wrapContentSize(), // Wraps the card content
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                WhiteCircleWithBlackBorder()
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp)
                )
                IconButton(
                    onClick = onEllipsisClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More options",
                        modifier = Modifier
                            .rotate(90f)
                            .clickable { TODO() }
                    )
                }
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRoundedCard() {
    AgendaDisplayItem(
        title = "Title",
        description = "This is a sample description for the rounded card UI model.",
        date = "March 28, 2024",
        onEllipsisClick = {}
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