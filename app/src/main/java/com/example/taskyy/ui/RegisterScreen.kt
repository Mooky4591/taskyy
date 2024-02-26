package com.example.taskyy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskyy.R
import com.example.taskyy.ui.events.RegisterEvent
import com.example.taskyy.ui.viewmodels.RegisterState

@Composable
fun RegisterScreen(state: RegisterState, onEvent: (RegisterEvent) -> Unit, navController: NavController) {
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
            CreateNameField(state, onEvent)
            Spacer(modifier = Modifier.height(10.dp))
            CreateEmailField(state.isEmailValid, onEvent, state.email)
            Spacer(modifier = Modifier.height(10.dp))
            CreatePasswordField(state.password, onEvent, state.isPasswordVisible)
            Spacer(modifier = Modifier.height(50.dp))
            CreateLoginButton(navController = navController, onLoginEvent = null, onRegisterEvent =  onEvent, text = stringResource(
                R.string.get_started
            )
            )
        }
    }
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNameField(state: RegisterState, onEvent: (RegisterEvent) -> Unit) {
    TextField(
        value = state.name,
        onValueChange = {
            onEvent(RegisterEvent.OnNameChanged(it))
        },
        placeholder = {
            Text(text = "Name", color = Color.LightGray)
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
    )
}