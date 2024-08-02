package com.example.oiidar.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oiidar.ui.screens.Home
import com.example.oiidar.ui.screens.Login
import com.example.oiidar.ui.screens.Program
import com.example.oiidar.ui.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OiidarNavHost(
    viewModel: MainViewModel,
    navController: NavHostController,
    authInit: () -> Unit = {},
){
    val user by viewModel.user.collectAsState()
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route
    ){
        composable(Destination.Login.route) {
            Login(
                viewModel = viewModel,
                authInit = { authInit() },
                navController = navController
            )
        }
        composable(Destination.Home.route) {
            Home( navController = navController,
                logOut = {
                    coroutineScope.launch {
                        viewModel.updateStatusUser(false, user?.nameId!!)
                        exitProcess(0)
                    }
                }
            )
        }
        composable(Destination.Prog.route) {
            Program(
                navController = navController,
                logOut = {
                    coroutineScope.launch {
                        viewModel.updateStatusUser(false, user?.nameId!!)
                        exitProcess(0)
                    }
                }
            )
        }
    }
}