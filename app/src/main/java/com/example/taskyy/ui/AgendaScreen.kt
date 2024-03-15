package com.example.taskyy.ui

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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskyy.R
import com.example.taskyy.domain.navigation.Screen
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.viewmodels.AgendaState
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun AgendaScreen(state: AgendaState, navController: NavController, onEvent: (AgendaEvent) -> Unit){
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.clickable {
                    onEvent(AgendaEvent.OnMonthExpanded(!state.isMonthExpanded))
                }
            ) {
                MonthText(
                    selectedMonth = state.selectedMonth
                )
                MonthDownArrow()
                if (state.isMonthExpanded) {
                    ShowDatePicker(
                        onItemSelected = { selectedMonth ->
                            onEvent(AgendaEvent.OnDateSelected(selectedMonth))
                        },
                    )
                }
            }
            CircleWithInitials(
                isUserDropDownExpanded = state.isUserDropDownExpanded,
                onUserInitialsClicked = { isUserDropDownExpanded ->
                    onEvent(AgendaEvent.OnUserInitialsClicked(isUserDropDownExpanded))
                },
                onItemSelected = {
                    onEvent(AgendaEvent.OnLogOutCLicked)
                },
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxHeight(.85F)
                .fillMaxWidth(),
            shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxHeight(.75f)
                    .padding(30.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
    if(state.wasLogoutSuccessful){
        navController.navigate(Screen.Login.route)
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

@Composable
fun MonthDownArrow() {
        Icon(
            painter = painterResource(id = R.drawable.drop_down_arrow),
            contentDescription = "", tint = Color.White
        )
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