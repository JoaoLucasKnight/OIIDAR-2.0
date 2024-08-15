package com.example.oiidar.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oiidar.ui.screens.HomeScreen
import com.example.oiidar.ui.screens.ProgramScreens
import com.example.oiidar.ui.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGraph(viewModel: MainViewModel, navController: NavHostController){
    NavHost(navController, Destination.Home.route) {
        composable(Destination.Home.route) {
            HomeScreen(
                nav = { navController.navigate(it) },
                logOut = { viewModel.logOut() }
            )
        }
        composable(Destination.Program.route) {
            ProgramScreens(
                nav = { navController.navigate(it) },
                logOut = { viewModel.logOut() }
            )
        }
    }
}