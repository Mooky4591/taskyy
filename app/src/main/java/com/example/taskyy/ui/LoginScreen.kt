package com.example.taskyy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskyy.R
import com.example.taskyy.domain.navigation.Screen
import com.example.taskyy.ui.events.LoginEvent
import com.example.taskyy.ui.events.RegisterEvent
import com.example.taskyy.ui.viewmodels.LoginState

@Composable
fun LoginScreen(state: LoginState, onEvent: (LoginEvent) -> Unit, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Black)
    ) {
        TopText(stringResource(R.string.welcome_back))
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
                CreateEmailField(state.isEmailValid, (String) -> Unit,  state.email)
                Spacer(modifier = Modifier.height(10.dp))
                CreatePasswordField(state.password, onEvent, state.isPasswordVisible)
                Spacer(modifier = Modifier.height(10.dp))
                CreateLoginButton(onEvent, null, navController, stringResource(R.string.login))
                Row(
                ) {
                    CreateBottomText()
                    CreateSignUpLink(navController)
                }
            }
        }
    }
}

@Composable
fun TopText(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEmailField(isEmailValid: Boolean, onValueChange: (String) -> Unit, email: String) {
    TextField(
        value = email,
        onValueChange = {
            onValueChange(
                email ->
            )

        },
        placeholder = {
            Text(text = stringResource(R.string.email_address), color = Color.LightGray)
        },
        trailingIcon = {
            val image = R.drawable.check_mark
            val description = if (isEmailValid) "Email is valid checkmark" else "email not valid"
            if (isEmailValid) {
                Icon(painter = painterResource(id = image), contentDescription = description)
            }
        },
        singleLine = true,
        modifier = if (isEmailValid || email.isNotEmpty())
            Modifier
            .fillMaxWidth()
        else Modifier
            .fillMaxWidth()
            .border(width = 1.5.dp, color = Color.Red),
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePasswordField(password: String, onEvent: () -> Unit, isPasswordVisible: Boolean) {

    TextField(
        value = password,
        onValueChange = { 
                        onEvent()
        },
        placeholder = {
            Text(text = stringResource(R.string.password), color = Color.LightGray)
        },
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
        visualTransformation = showOrHidePassword(isPasswordVisible = isPasswordVisible),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            CreateHidePasswordToggle(isPasswordVisible = isPasswordVisible, onEvent = onEvent)
        }
    )
}
@Composable
fun showOrHidePassword(isPasswordVisible: Boolean): VisualTransformation {
    return if (!isPasswordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }
}


@Composable
fun CreateHidePasswordToggle(isPasswordVisible: Boolean, onEvent: () -> Unit) {
    val image = if (isPasswordVisible)
        R.drawable.show_password
    else R.drawable.hide_password

    // Localized description for accessibility services
    val description = if (isPasswordVisible) stringResource(R.string.hide_password) else stringResource(
        R.string.show_password
    )

    // Toggle button to hide or display password
    IconButton(onClick = {
        onEvent(
            if (isPasswordVisible) {
                LoginEvent.OnTogglePasswordVisibility(false)
            } else LoginEvent.OnTogglePasswordVisibility(true)
        )
    })
    {
        Icon(
            painter = painterResource(id = image),
            contentDescription = description)
    }
}

@Composable
fun CreateLoginButton(onLoginEvent: ((LoginEvent) -> Unit)?, onRegisterEvent: ((RegisterEvent) -> Unit)?, navController: NavController, text: String) {
    Button(
        onClick = {
                  if(onLoginEvent != null) {
                    //onLoginEvent(LoginEvent.OnLoginClick)
                  } else {
                     //onRegisterEvent(RegisterEvent.OnGetStartedClick)
                  }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun CreateBottomText() {
    Text(text = "DON'T HAVE AN ACCOUNT")
}

@Composable
fun CreateSignUpLink(navController: NavController) {
    return Text(
        text = "SIGN UP",
        modifier = Modifier.clickable {
            navController.navigate(Screen.Register.route)
        },
        color = Color.Blue,
        fontStyle = FontStyle.Italic)
}