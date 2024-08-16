package com.example.oiidar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.oiidar.ui.viewModel.MainViewModel

@Composable
fun NavHost(
    viewModel: MainViewModel,
    navController: NavHostController,
    authInit: () -> Unit = {},

){
    val user by  viewModel.user.collectAsState()
    viewModel.checkUser()

    if (user != null) {
        MainGraph(viewModel = viewModel, navController = navController, authInit = { authInit()})
    } else {
        LogGraph(viewModel = viewModel, navController = navController, authInit = { authInit() })
    }
}


