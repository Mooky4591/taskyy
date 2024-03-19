package com.example.taskyy.ui.screens

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
import com.example.taskyy.R
import com.example.taskyy.domain.objects.Login
import com.example.taskyy.ui.events.LoginEvent
import com.example.taskyy.ui.viewmodels.LoginState

@Composable
fun LoginScreen(state: LoginState, onEvent: (LoginEvent) -> Unit) {
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
                CreateEmailField(isEmailValid = state.isEmailValid, onValueChange =  { email -> onEvent(LoginEvent.OnEmailChanged(email)) }, email =  state.email)
                Spacer(modifier = Modifier.height(10.dp))
                CreatePasswordField(password = state.password, onValueChange = {password -> onEvent(LoginEvent.OnPasswordChanged(password))},
                    isPasswordVisible =  state.isPasswordVisible,
                    onClick = {isPasswordVisible -> onEvent(LoginEvent.OnTogglePasswordVisibility(isPasswordVisible))}
                )
                Spacer(modifier = Modifier.height(10.dp))
                val loginObject = Login(state.email, state.password)
                TaskyyActionButton(onClick = {onEvent(LoginEvent.OnLoginClick(loginObject))}, text = stringResource(
                    id = R.string.login
                ), login = loginObject)
                Row(
                ) {
                    CreateBottomText()
                    CreateSignUpLink(onEvent = { onEvent(LoginEvent.OnRegisterLinkClick) })
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
@Composable
fun CreateEmailField(isEmailValid: Boolean, onValueChange: (String) -> Unit, email: String) {
    TextField(
        value = email,
        onValueChange = {
                        onValueChange(it)
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
        modifier = if (isEmailValid || email.isEmpty())
            Modifier
            .fillMaxWidth()
        else Modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                color = Color.Red,
                shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
            ),
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
    )
}

@Composable
fun CreatePasswordField(password: String, onValueChange: (String) -> Unit, isPasswordVisible: Boolean, onClick: (Boolean) -> Unit) {

    TextField(
        value = password,
        onValueChange = { 
                      onValueChange(it)
        },
        placeholder = {
            Text(text = stringResource(R.string.password), color = Color.LightGray)
        },
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
        visualTransformation = showOrHidePassword(isPasswordVisible = isPasswordVisible),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            CreateHidePasswordToggle(isPasswordVisible = isPasswordVisible, onClick = {isPasswordVisible -> onClick(isPasswordVisible)})
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
fun CreateHidePasswordToggle(isPasswordVisible: Boolean, onClick: (Boolean) -> Unit) {
    val image = if (isPasswordVisible)
        R.drawable.show_password
    else R.drawable.hide_password

    // Localized description for accessibility services
    val description = if (isPasswordVisible) stringResource(R.string.hide_password) else stringResource(
        R.string.show_password
    )

    // Toggle button to hide or display password
    IconButton(onClick = {
            onClick(!isPasswordVisible)
    })
    {
        Icon(
            painter = painterResource(id = image),
            contentDescription = description)
    }
}

@Composable
fun TaskyyActionButton(onClick: (Login) -> Unit, text: String, login: Login) {
    Button(
        onClick = {
                  onClick(login)
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
fun CreateSignUpLink(onEvent: (LoginEvent) -> Unit) {
    return Text(
        text = "SIGN UP",
        modifier = Modifier.clickable {
            onEvent(LoginEvent.OnRegisterLinkClick)
        },
        color = Color.Blue,
        fontStyle = FontStyle.Italic
    )
}