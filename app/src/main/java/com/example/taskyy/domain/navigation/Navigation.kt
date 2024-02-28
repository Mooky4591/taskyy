package com.example.taskyy.domain.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.taskyy.ui.LoginScreen
import com.example.taskyy.ui.RegisterScreen
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
                LoginScreen(state, loginViewModel::onEvent, navController)
            }
            composable(route = Screen.Register.route) {
                val registerViewModel = hiltViewModel<RegisterViewModel>()
                val state = registerViewModel.state
                RegisterScreen(
                    state = state,
                    onEvent = registerViewModel::onEvent,
                )
            }
        }
    }
}


