package com.example.oiidar.navigation

sealed class Destination (val route: String) {
    data object Home : Destination("home")
    data object Login : Destination("logar")
    data object Prog: Destination("programacao")
}