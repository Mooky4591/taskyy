package com.example.taskyy.domain.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.events.EditTextEvent
import com.example.taskyy.ui.events.LoginEvent
import com.example.taskyy.ui.events.RegisterEvent
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.screens.AgendaScreen
import com.example.taskyy.ui.screens.EditTextScreen
import com.example.taskyy.ui.screens.LoginScreen
import com.example.taskyy.ui.screens.RegisterScreen
import com.example.taskyy.ui.screens.ReminderScreen
import com.example.taskyy.ui.viewmodels.AgendaViewModel
import com.example.taskyy.ui.viewmodels.EditTextViewModel
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
                AgendaScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is AgendaEvent.ReminderItemSelected -> navController.navigate(Screen.Reminder.route + "/${state.dateString}")
                            else -> agendaViewModel.onEvent(event)
                        }
                        agendaViewModel.onEvent(event = event)
                    }
                )
            }

            composable(route = Screen.Reminder.route + "/{dateString}") {
                val reminderViewModel = hiltViewModel<ReminderViewModel>()
                val state by reminderViewModel.state.collectAsState()
                val editedRemindDescription = navController
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.getStateFlow<String>("reminderDescription", "Reminder Description")
                    ?.collectAsStateWithLifecycle()

                LaunchedEffect(editedRemindDescription) {
                    reminderViewModel.setReminderDescription(
                        editedRemindDescription?.value ?: "Reminder Description"
                    )
                }

                ReminderScreen(
                    state = state
                ) { event ->
                    when (event) {
                        is ReminderEvent.EnterReminderDescription -> {
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "screenType",
                                event.editDescription
                            )
                            navController.navigate(Screen.EditText.route)
                        }

                        is ReminderEvent.EnterSetTitleScreen -> {
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "screenType",
                                event.editTitle
                            )
                            navController.navigate(Screen.EditText.route)
                        }

                        else -> reminderViewModel.onEvent(event)
                    }
                    reminderViewModel.onEvent(event)
                }
            }

            composable(route = Screen.EditText.route) {
                val editTextViewModel = hiltViewModel<EditTextViewModel>()
                val state by editTextViewModel.state.collectAsState()
                val screenType = navController
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.getStateFlow<EditTextScreenType>("screenType", EditTextScreenType.NULL)
                    ?.collectAsStateWithLifecycle()

                LaunchedEffect(screenType) {
                    if (screenType != null) {
                        editTextViewModel.updateTitle(
                            type = screenType.value
                        )
                    }
                }

                EditTextScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is EditTextEvent.SaveDescription -> {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "reminderDescription",
                                    state.enteredText
                                )
                                navController.popBackStack()
                            }

                            is EditTextEvent.SaveTitle -> {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "reminderTitle",
                                    state.enteredText
                                )
                            }
                        }
                        editTextViewModel.onEvent(event)
                    },
                )
            }
        }
    }
}


