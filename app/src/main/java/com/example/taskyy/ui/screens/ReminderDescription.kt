package com.example.taskyy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.viewmodels.ReminderState

@Composable
fun ReminderDescription(state: ReminderState, onEvent: (ReminderEvent) -> Unit) {

    Scaffold(
        topBar = {
            TopBar(
                dateString = "EDIT DESCRIPTION",
                onEvent = onEvent,
                color = Color.Black,
                saveFunction = {
                    SaveDetails(
                        onEvent = onEvent,
                        details = state.reminderDescription
                    )
                },
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(top = 60.dp, start = 10.dp)
            ) {
                BasicTextField(
                    value = state.reminderDescription,
                    onValueChange = { description ->
                        onEvent(ReminderEvent.ReminderDescriptionUpdated(description))
                    },
                    textStyle = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr))
                )
            }
        }
    )
}
