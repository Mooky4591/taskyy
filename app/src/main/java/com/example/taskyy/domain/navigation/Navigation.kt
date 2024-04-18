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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.taskyy.ui.enums.AgendaItemType
import com.example.taskyy.ui.enums.EditTextScreenType
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.events.EditTextEvent
import com.example.taskyy.ui.events.LoginEvent
import com.example.taskyy.ui.events.RegisterEvent
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.screens.AgendaScreen
import com.example.taskyy.ui.screens.EditTextScreen
import com.example.taskyy.ui.screens.EventScreen
import com.example.taskyy.ui.screens.LoginScreen
import com.example.taskyy.ui.screens.RegisterScreen
import com.example.taskyy.ui.screens.ReminderScreen
import com.example.taskyy.ui.viewmodels.AgendaViewModel
import com.example.taskyy.ui.viewmodels.EditTextViewModel
import com.example.taskyy.ui.viewmodels.EventViewModel
import com.example.taskyy.ui.viewmodels.LoginViewModel
import com.example.taskyy.ui.viewmodels.RegisterViewModel
import com.example.taskyy.ui.viewmodels.ReminderViewModel
import java.time.ZoneId


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
                val timeDateState = agendaViewModel.timeDateState
                ObserveAsEvents(agendaViewModel.events) { agendaEvent ->
                    when (agendaEvent) {
                        is AgendaEvent.LogoutSuccessful -> {
                            navController.navigate(Screen.Login.route)
                        }

                        else -> {}
                    }
                }
                AgendaScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is AgendaEvent.MenuItemSelected ->
                                when (event.itemType) {
                                    AgendaItemType.REMINDER_ITEM -> navController.navigate(Screen.Reminder.route + "/${timeDateState.dateTime}, ${event.itemType}, ${event.eventItemId}, ${event.isEditing}")
                                    AgendaItemType.TASK_ITEM -> navController.navigate(Screen.Reminder.route + "/${timeDateState.dateTime}, ${event.itemType}, ${event.eventItemId}, ${event.isEditing}")
                                    AgendaItemType.EVENT_ITEM -> navController.navigate(Screen.Event.route + "/${timeDateState.dateTime}, ${event.itemType}, ${event.eventItemId}, ${event.isEditing}")
                                }
                            else -> agendaViewModel.onEvent(event)
                        }
                    },
                    timeDateState = timeDateState
                )
            }

            composable(
                route = Screen.Event.route + "/{dateString}, {agendaItem}, {eventItemId}, {isEditing}",
                deepLinks =
                listOf(
                    navDeepLink {
                        uriPattern =
                            "tasky://www.myapp.com/eventScreen?zoneDateTime={zoneDateTime}" +
                                    "&isEditingMode={isEditingMode}&agendaItemId={agendaItemId}"
                    },
                ),
            ) {
                val eventViewModel = hiltViewModel<EventViewModel>()
                val state by eventViewModel.state.collectAsState()
                val timeDateState by eventViewModel.timeAndDateState.collectAsState()
                EventScreen(
                    state = state,
                    onEvent = { event -> eventViewModel.onEvent(event) },
                    timeDateState = timeDateState
                )
            }

            composable(
                route = Screen.Reminder.route + "/{dateString}, {agendaItem}, {eventItemId}, {isEditing}",
                deepLinks =
                listOf(
                    navDeepLink {
                        uriPattern =
                            "tasky://www.myapp.com/reminderScreen?zoneDateTime={zoneDateTime}" +
                                    "&isEditingMode={isEditingMode}&agendaItemId={agendaItemId}"
                    },
                ),
            ) {
                val reminderViewModel = hiltViewModel<ReminderViewModel>()
                val state by reminderViewModel.state.collectAsState()
                val timeDateState by reminderViewModel.timeAndDateState.collectAsState()
                val editedRemindDescription = navController
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.getStateFlow<String>("reminderDescription", "")
                    ?.collectAsStateWithLifecycle()
                val editedRemindTitle = navController
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.getStateFlow("reminderTitle", "")
                    ?.collectAsStateWithLifecycle()

                LaunchedEffect(editedRemindDescription) {
                    if (editedRemindDescription?.value != "")
                    reminderViewModel.setReminderDescription(
                        editedRemindDescription?.value ?: ""
                    )
                }
                LaunchedEffect(editedRemindTitle) {
                    if (editedRemindTitle?.value != "")
                    reminderViewModel.setReminderTitle(
                        editedRemindTitle?.value ?: ""
                    )
                }

                ObserveAsEvents(reminderViewModel.events) { event ->
                    when (event) {
                        is ReminderEvent.SaveFailed -> {
                        }

                        is ReminderEvent.SaveSuccessful -> {
                            navController.getBackStackEntry(Screen.Agenda.route).savedStateHandle["selectedDate"] =
                                timeDateState.dateTime.toLocalDate()
                                    .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            navController.navigate(Screen.Agenda.route)
                        }

                        else -> {}
                    }
                }

                ReminderScreen(
                    state = state,
                    onEvent =
                    { event ->
                    when (event) {
                        is ReminderEvent.EnterReminderDescription -> {
                            navController.navigate(Screen.EditText.route + "/${event.editDescription}, ${state.reminderDescription}")
                        }

                        is ReminderEvent.EnterSetTitleScreen -> {
                            navController.navigate(Screen.EditText.route + "/${event.editTitle}, ${state.reminderTitleText}")
                        }

                        is ReminderEvent.Close -> {
                            navController.navigateUp()
                        }

                        else -> reminderViewModel.onEvent(event)
                    }
                    },
                    timeDateState = timeDateState
                )
            }

            composable(
                route = Screen.EditText.route + "/{screenType}, {textToEdit}",
                arguments = listOf(
                    navArgument("screenType") {
                        type = NavType.EnumType<EditTextScreenType>(EditTextScreenType::class.java)
                    },
                    navArgument("textToEdit") {
                        type = NavType.StringType
                    }
                )
            ) {
                val editTextViewModel = hiltViewModel<EditTextViewModel>()
                val state by editTextViewModel.state.collectAsState()

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
                                navController.popBackStack()
                            }

                            is EditTextEvent.Back -> {
                                navController.navigateUp()
                            }

                            else -> {
                            }
                        }
                        editTextViewModel.onEvent(event)
                    },
                )
            }
        }
    }
}


