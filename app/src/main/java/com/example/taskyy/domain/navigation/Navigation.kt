package com.example.taskyy.domain.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Nav() {

    val navController = rememberNavController()

    @Composable
    fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(flow, lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {
                    flow.collect(onEvent)
                }
            }
        }
    }


    NavHost(navController = navController, startDestination = "login_screen") {
        navigation(
            startDestination = Screen.Login.route,
            route = "login_screen"
        ) {
            composable(route = Screen.Login.route) {
                val loginViewModel = hiltViewModel<LoginViewModel>()
                ObserveAsEvents(loginViewModel.events) { event ->
                    when (event) {
                        is LoginEvent.LoginSuccess -> navController.navigate(Screen.Agenda.route)
                        else -> {}
                    }
                }
                val state = loginViewModel.state
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
                ObserveAsEvents(registerViewModel.events) { event ->
                    when (event) {
                        is RegisterEvent.RegistrationSuccessful -> navController.navigate(Screen.Agenda.route)
                        else -> {}
                    }
                }
                val state = registerViewModel.state
                RegisterScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is RegisterEvent.OnGetStartedClick -> if (state.isRegistrationSuccessful)
                                navController.navigate(Screen.Agenda.route)

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
                            is AgendaEvent.OnLogOutCLicked -> if (state.wasLogoutSuccessful)
                                navController.navigate(Screen.Login.route)

                            else -> {}
                        }
                        agendaViewModel.onEvent(event)
                    }
                )
            }
        }
    }
}


