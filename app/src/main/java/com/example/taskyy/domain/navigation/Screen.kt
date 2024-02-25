package com.example.taskyy.domain.navigation

sealed class Screen(val route: String) {
    data object Login: Screen(route = "login")
    data object Register: Screen(route = "register")
}