package com.example.oiidar.navigation

sealed class Destination (val route: String) {
    data object Home : Destination("HOME")
    data object Login : Destination("LOGIN")
    data object Program: Destination("PROGRAM")

    data object Main: Destination("MAIN")
    data object LogOut: Destination("LOGOUT")
}