package com.example.oiidar.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.oiidar.ui.screens.HomeScreen
import com.example.oiidar.ui.screens.LoginScreen
import com.example.oiidar.ui.screens.ProgramScreens
import com.example.oiidar.ui.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHost(
    viewModel: MainViewModel,
    navController: NavHostController,
    authInit: () -> Unit = {},
){
    fun exitApp (){
        viewModel.logOut()
        navController.navigate(Destination.LogOut.route){
            popUpTo(Destination.Main.route) { inclusive = true }
        }
    }
    NavHost(navController, Destination.Login.route, route = Destination.LogOut.route) {
        composable(Destination.Login.route,
            enterTransition = {
                slideIntoContainer(animationSpec = tween(500, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Right)
            }
            ) {
            LoginScreen(
                viewModel = viewModel,
                authInit = { authInit() },
                nav = {
                    navController.navigate(it){
                        popUpTo(Destination.LogOut.route) { inclusive = true }
                    }
                }
            )
        }
        navigation(startDestination = Destination.Home.route, route = Destination.Main.route) {
            composable(Destination.Home.route,
                exitTransition = { ExitTransition.KeepUntilTransitionsFinished }
            ) {
                HomeScreen(
                    nav = { navController.navigate(it) },
                    logOut = { exitApp()}
                )
            }
            composable(
                Destination.Program.route,
                enterTransition = {
                    scaleIn(animationSpec = tween(500, easing = EaseInExpo)) +
                            fadeIn(animationSpec = tween(500, easing = EaseInCirc))
                },
                exitTransition = {
                    scaleOut(animationSpec = tween(500, easing = EaseOutCirc)) +
                            fadeOut(animationSpec = tween(500, easing = EaseOut))
                }
            ) {
                ProgramScreens(
                    nav = { navController.navigate(it) },
                    logOut = { exitApp() }
                )
            }
        }
    }

}


