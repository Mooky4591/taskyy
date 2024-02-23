package com.example.taskyy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskyy.R
import com.example.taskyy.ui.events.LoginEvents
import kotlin.reflect.KFunction1

@Composable
fun MainScreen(state: String, onEvent: KFunction1<LoginEvents, Unit>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Black)
    ) {
        WelcomeBackText()
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
                CreateUserNameField(state, onEvent)
                Spacer(modifier = Modifier.height(10.dp))
                CreatePasswordField()
                Spacer(modifier = Modifier.height(10.dp))
                CreateLoginButton()
                Row(
                ) {
                    CreateBottomText()
                    CreateSignUpLink()
                }
            }
        }
    }
}

@Composable
private fun WelcomeBackText() {
    Text(
        text = "Welcome Back!",
        color = Color.White,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserNameField(state: String, onEvent: (LoginEvents) -> Unit) {
    TextField(
        value = state,
        onValueChange = { onEvent(LoginEvents.textChanged())},
        placeholder = {
            Text(text = "Email Address", color = Color.LightGray)
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePasswordField() {
    var textFieldState by remember {
        mutableStateOf("")
    }

    TextField(
        value = textFieldState,
        onValueChange = { textFieldState = it },
        placeholder = {
            Text(text = "Password", color = Color.LightGray)
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            CreateHidePasswordToggle()

        })
}

@Composable
fun CreateHidePasswordToggle() {
    var passwordVisible by remember {
        mutableStateOf(false) }
    val image = if (passwordVisible)
        R.drawable.show_password
    else R.drawable.hide_password

    // Localized description for accessibility services
    val description = if (passwordVisible) "Hide password" else "Show password"

    // Toggle button to hide or display password
    IconButton(onClick = {passwordVisible = !passwordVisible}){
        Icon(
            painter = painterResource(id = image),
            contentDescription = description)
    }
}

@Composable
fun CreateLoginButton() {
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Login", color = Color.White)
    }
}

@Composable
fun CreateBottomText() {
    Text(text = "DON'T HAVE AN ACCOUNT")
}

@Composable
fun CreateSignUpLink() {
    return Text(
        text = "SIGN UP",
        modifier = Modifier.clickable { /*TODO*/},
        color = Color.Blue,
        fontStyle = FontStyle.Italic)
}