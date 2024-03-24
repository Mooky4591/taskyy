package com.example.taskyy.domain.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.events.LoginEvent
import com.example.taskyy.ui.events.RegisterEvent
import com.example.taskyy.ui.screens.AgendaScreen
import com.example.taskyy.ui.screens.LoginScreen
import com.example.taskyy.ui.screens.RegisterScreen
import com.example.taskyy.ui.screens.ReminderScreen
import com.example.taskyy.ui.viewmodels.AgendaViewModel
import com.example.taskyy.ui.viewmodels.LoginViewModel
import com.example.taskyy.ui.viewmodels.RegisterViewModel
import com.example.taskyy.ui.viewmodels.ReminderViewModel


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Nav() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_screen") {
        navigation(
            startDestination = Screen.Login.route,
            route = "login_screen"
        ) {
            composable(route = Screen.Login.route) {
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val state = loginViewModel.state
                val context = LocalContext.current
                ObserveAsEvents(loginViewModel.events) { event ->
                    when (event) {
                        is LoginEvent.LoginFailed -> Toast.makeText(
                            context,
                            event.errorText.asString(context),
                            Toast.LENGTH_SHORT
                        ).show()

                        is LoginEvent.LoginSuccess -> {
                            navController.navigate(Screen.Agenda.route)
                        }
                        else -> {}
                    }
                }
                LoginScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is LoginEvent.OnRegisterLinkClick -> navController.navigate(Screen.Register.route)
                            else -> {}
                        }
                        loginViewModel.onEvent(event)
                    },
                )
            }
            composable(route = Screen.Register.route) {
                val registerViewModel = hiltViewModel<RegisterViewModel>()
                val state = registerViewModel.state
                val context = LocalContext.current
                ObserveAsEvents(registerViewModel.events) { event ->
                    when (event) {
                        is RegisterEvent.RegistrationSuccessful -> navController.navigate(Screen.Login.route)
                        is RegisterEvent.RegistrationFailed -> Toast.makeText(
                            context,
                            event.errorMessage.asString(context),
                            Toast.LENGTH_SHORT
                        ).show()

                        else -> {}
                    }
                }
                RegisterScreen(
                    state = state,
                    onEvent = { registerViewModel.onEvent(it) }
                )
            }
            composable(route = Screen.Agenda.route) {
                val agendaViewModel = hiltViewModel<AgendaViewModel>()
                val state = agendaViewModel.state

                ObserveAsEvents(agendaViewModel.events) { event ->
                    when (event) {
                        is AgendaEvent.LogoutSuccessful -> navController.navigate(Screen.Login.route)
                        else -> {}
                    }
                }
                AgendaScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is AgendaEvent.ReminderItemSelected -> navController.navigate(Screen.Reminder.route)
                            else -> agendaViewModel.onEvent(event)
                        }
                    }
                )
            }

            composable(route = Screen.Reminder.route) {
                val reminderViewModel = hiltViewModel<ReminderViewModel>()
                val state = reminderViewModel.state
                ReminderScreen(
                    state = state,
                    onEvent = {
                        reminderViewModel.onEvent(it)
                    }
                )
            }
        }
    }
}


