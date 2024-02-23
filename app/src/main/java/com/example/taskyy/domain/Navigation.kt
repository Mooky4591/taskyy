package com.example.taskyy.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskyy.ui.MainScreen
import com.example.taskyy.ui.viewmodels.LoginViewModel

@Composable
fun Nav() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login" ){

        composable(route = "Login"){
            val viewModel = hiltViewModel<LoginViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            MainScreen(state.toString(), viewModel::onEvent)
        }

    }

}

