package com.example.taskyy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskyy.R
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.ui.events.RegisterEvent
import com.example.taskyy.ui.viewmodels.RegisterState

@Composable
fun RegisterScreen(state: RegisterState, onEvent: (RegisterEvent) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Black)
    ) {
        TopText(stringResource(R.string.create_your_account))
        Surface(
            modifier = Modifier
            .fillMaxHeight(.80F),
        shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxHeight(.75f)
                .padding(30.dp)
        ) {
            CreateNameField(name = state.name, onValueChange = { name -> onEvent(RegisterEvent.OnNameChanged(name))})
            Spacer(modifier = Modifier.height(10.dp))
            CreateEmailField(isEmailValid = state.isEmailValid, onValueChange = {email -> onEvent(RegisterEvent.OnEmailChanged(email = email))}, email = state.email)
            Spacer(modifier = Modifier.height(10.dp))
            CreatePasswordField(password = state.password,
                onValueChange = { password -> onEvent(RegisterEvent.OnPasswordChanged(password)) },
                isPasswordVisible = state.isPasswordVisible,
                onClick = { isPasswordVisible ->
                    onEvent(
                        RegisterEvent.OnTogglePasswordVisibility(
                            isPasswordVisible
                        )
                    )
                })
            Text(text = state.errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(50.dp))
            val loginObject = Login(state.email, state.password)
            TaskyyActionButton(onClick = {onEvent(RegisterEvent.OnGetStartedClick)}, text = stringResource(
                id = R.string.get_started
            ), login = loginObject
            )
        }
    }
}
}

@Composable
fun CreateNameField(name: String, onValueChange: (String) -> Unit) {
    TextField(
        value = name,
        onValueChange = {
                        onValueChange(it)
        },
        placeholder = {
            Text(text = "Name", color = Color.LightGray)
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
    )
}