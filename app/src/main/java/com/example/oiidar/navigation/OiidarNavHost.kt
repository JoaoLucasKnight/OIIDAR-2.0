package com.example.oiidar.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oiidar.ui.screens.Home
import com.example.oiidar.ui.screens.Logar
import com.example.oiidar.ui.screens.Programacao
import com.example.oiidar.ui.viewModel.AuthVM
import com.example.oiidar.ui.viewModel.HomeVM
import com.example.oiidar.ui.viewModel.ProgramacaoVM
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OiidarNavHost(
    navController: NavHostController,
    authy: () -> Unit,
){
    val vm: AuthVM = hiltViewModel()
    val destino by vm.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = if (destino != null) Destination.Home.route
        else Destination.Login.route
    ){
        composable(Destination.Login.route) {
            Logar(
                viewModel = vm,
                onAutenticar ={ authy() },
            )
        }
        composable(Destination.Home.route) {
                val viewModel: HomeVM = hiltViewModel()
                Home(
                    viewModel = viewModel,
                    navController = navController,
                    deslogar = {
                        coroutineScope.launch {
                            vm.deslogandoUser()
                            exitProcess(0)
                        }
                    }
                )
        }
        composable(Destination.Prog.route) {
            val viewModel: ProgramacaoVM = hiltViewModel()
            Programacao(
                viewModel = viewModel,
                deslogar = {
                    coroutineScope.launch {
                        vm.deslogandoUser()
                        exitProcess(0)
                    }
                }
            )
        }
    }
}