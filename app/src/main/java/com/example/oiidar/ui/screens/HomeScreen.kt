package com.example.oiidar.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.oiidar.ui.components.Body
import com.example.oiidar.ui.components.Header
import com.example.oiidar.ui.components.NavBottom
import com.example.oiidar.ui.viewModel.HomeViewModel
import kotlinx.coroutines.delay


@Composable
@ExperimentalMaterial3Api
fun HomeScreen(
    nav : (String) -> Unit,
    logOut: () -> Unit
){
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    viewModel.loading()
    when(state.loading){
        "LOAD" -> {
            Scaffold(
                topBar = {
                    Header(
                        img = state.user?.img,
                        show = state.showEnd,
                        onShow = state.onShowEnd,
                        logOut = { logOut() }
                    )
                },
                content = { innerPadding ->
                    Body(
                        pad = innerPadding,
                        nav = { nav(it) },
                        state = state
                    )
                },
                bottomBar = {
                    NavBottom(
                        play = { track -> viewModel.playTrack(track) },
                        state = state
                    )
                }
            )
            LaunchedEffect(key1 = state) {
                viewModel.checkAndUpdateProgramStatus()
                if(state.status) {
                    var ms = viewModel.discoverTrackPlaying()
                    delay(ms)
                    while((ms != 0L ) and (state.track != null)){
                        ms = viewModel.nextTrack()
                        delay(ms)
                    }
                }
            }
        }
        "LOADING" -> {
            Surface{
                viewModel.loading()
                LoadingScreen()
            }
        }
        "ERROR" -> {
            Surface {
                ErrorScreen()
                LaunchedEffect(key1 = Unit) {
                    delay(1500)
                    logOut()
                }
            }
        }
    }
}


