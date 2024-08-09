package com.example.oiidar.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oiidar.ui.screens.HomeScreen
import com.example.oiidar.ui.screens.LoginScreen
import com.example.oiidar.ui.screens.ProgramScreens
import com.example.oiidar.ui.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHost(
    viewModel: MainViewModel,
    navController: NavHostController,
    authInit: () -> Unit = {},
){
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route
    ){
        composable(Destination.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                authInit = { authInit() },
                navController = navController
            )
        }
        composable(Destination.Home.route) {
            HomeScreen( navController = navController,
                logOut = {
                    viewModel.logout()
                    navController.navigate(Destination.Login.route)
                }
            )
        }
        composable(Destination.Program.route) {
            ProgramScreens(
                navController = navController,
                logOut = {
                    viewModel.logout()
                    navController.navigate(Destination.Login.route)
                }
            )
        }
    }
}