package com.example.taskyy.domain.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.taskyy.ui.AgendaScreen
import com.example.taskyy.ui.LoginScreen
import com.example.taskyy.ui.RegisterScreen
import com.example.taskyy.ui.events.AgendaEvent
import com.example.taskyy.ui.events.LoginEvent
import com.example.taskyy.ui.events.RegisterEvent
import com.example.taskyy.ui.viewmodels.AgendaViewModel
import com.example.taskyy.ui.viewmodels.LoginViewModel
import com.example.taskyy.ui.viewmodels.RegisterViewModel


@Composable
fun Nav() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login_screen" ) {
        navigation(
            startDestination = Screen.Login.route,
            route = "login_screen"
        ) {
            composable(route = Screen.Login.route) {
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val state = loginViewModel.state
                LoginScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is LoginEvent.OnLoginClick -> if (state.isLoginSuccessful) {
                                (navController.navigate(Screen.Agenda.route))
                            }

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
                RegisterScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is RegisterEvent.OnGetStartedClick -> if (state.isRegistrationSuccessful) {
                                navController.navigate(Screen.Agenda.route)
                            }

                            else -> {}
                        }
                        registerViewModel.onEvent(event)
                    }
                )
            }
            composable(route = Screen.Agenda.route) {
                val agendaViewModel = hiltViewModel<AgendaViewModel>()
                val state = agendaViewModel.state
                AgendaScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is AgendaEvent.OnLogOutCLicked -> if (state.wasLogoutSuccessful) {
                                navController.navigate(Screen.Login.route)
                            }

                            else -> {}
                        }
                        agendaViewModel.onEvent(event)
                    }
                )
            }
        }
    }
}


