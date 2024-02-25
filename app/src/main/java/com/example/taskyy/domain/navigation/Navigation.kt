package com.example.taskyy.domain.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.taskyy.ui.LoginScreen
import com.example.taskyy.ui.RegisterScreen
import com.example.taskyy.ui.viewmodels.LoginViewModel


@Composable
fun Nav() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login_screen" ) {
        navigation(
            startDestination = Screen.Login.route,
            route = "login_screen"
        ) {
            composable(route = Screen.Login.route) {
                val viewModel = hiltViewModel<LoginViewModel>()
                val state = viewModel.state
                LoginScreen(state, viewModel::onEvent, navController)
            }
            composable(route = Screen.Register.route) { entry ->
                val loginViewModel = entry.loginViewModel<LoginViewModel>(navController = navController)
                val state = loginViewModel.state
                RegisterScreen(
                    state = state,
                    onEvent = loginViewModel::onEvent,
                    navController = navController
                )
            }
        }
    }
}
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.loginViewModel(
    navController: NavController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

