package com.example.oiidar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oiidar.ui.screens.LoginScreen
import com.example.oiidar.ui.viewModel.MainViewModel

@Composable
fun LogGraph(
    viewModel: MainViewModel,
    authInit: () -> Unit = {},
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Destination.Login.route
    ) {
        composable(Destination.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                authInit = { authInit() },
                nav = { navController.navigate(it) }
            )
        }

    }
}