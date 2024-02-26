package com.example.taskyy.domain

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.taskyy.ui.LoginScreen
import com.example.taskyy.ui.viewmodels.LoginViewModel
import com.example.taskyy.ui.viewmodels.RegisterViewModel


@Composable
fun Nav() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login" ){
        navigation(
            startDestination = "login",
            route = "auth"
        ) {
            composable(route = "login"){
                val viewModel = hiltViewModel<LoginViewModel>()
                val state = viewModel.state
                LoginScreen(state, viewModel::onEvent)
        }
            composable(route = "register") {
                val viewModel = hiltViewModel<RegisterViewModel>()
                val state = viewModel.state
            }

        }

    }

}

