package com.example.taskyy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.events.EditTextEvent
import com.example.taskyy.ui.viewmodels.EditTextState

@Composable
fun EditTextScreen(state: EditTextState, onEvent: (EditTextEvent) -> Unit) {

    Scaffold(
        topBar = {
            TopBar(
                title = determineTitleText(screenType = state.screenType),
                onClose = { onEvent(EditTextEvent.Back) },
                color = Color.Black,
                saveFunction = {
                    DetermineWhichTopBarToUse(
                        screenType = state.screenType,
                        onEvent = onEvent,
                        details = state.enteredText
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(top = 60.dp, start = 10.dp)
            ) {
                BasicTextField(
                    value = state.enteredText,
                    onValueChange = { description ->
                        onEvent(EditTextEvent.TextUpdated(description))
                    },
                    textStyle = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .padding(it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr))
                        .fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun determineTitleText(screenType: EditTextScreenType?): String {
    return when (screenType) {
        EditTextScreenType.EDIT_DETAILS -> "EDIT DETAILS"
        EditTextScreenType.EDIT_TITLE -> "EDIT TITLE"
        else -> {
            ""
        }
    }
}

@Composable
fun DetermineWhichTopBarToUse(
    screenType: EditTextScreenType?,
    onEvent: (EditTextEvent) -> Unit,
    details: String
) {
    return when (screenType) {
        EditTextScreenType.EDIT_DETAILS ->
            SaveDetails(onEvent = {
                onEvent(
                    EditTextEvent.SaveDescription(
                        details
                    )
                )
            }, details = details)

        EditTextScreenType.EDIT_TITLE -> SaveTitle(
            details = details,
            onEvent = { onEvent(EditTextEvent.SaveTitle(details)) })

        else -> {}
    }
}
