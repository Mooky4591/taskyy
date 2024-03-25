package com.example.taskyy.domain.navigation

sealed class Screen(val route: String) {
    data object Login : Screen(route = "login")
    data object Register : Screen(route = "register")
    data object Agenda : Screen(route = "agenda")

    //reminder screens
    data object Reminder : Screen(route = "reminder")
    data object ReminderDetails : Screen(route = "reminder_details")
    data object Task : Screen(route = "task")
    data object Event : Screen(route = "event")
}