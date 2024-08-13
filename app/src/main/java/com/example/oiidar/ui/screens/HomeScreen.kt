package com.example.oiidar.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oiidar.navigation.Destination
import com.example.oiidar.ui.components.Body
import com.example.oiidar.ui.components.Header
import com.example.oiidar.ui.components.NavBotton
import com.example.oiidar.ui.viewModel.HomeViewModel
import kotlinx.coroutines.delay


@Composable
@ExperimentalMaterial3Api
fun HomeScreen(
    navController : NavController
){
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    viewModel.loading()
    Column (
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(0.dp, 32.dp, 0.dp, 0.dp))
    {
        when(state.loading){
            "LOAD" -> {
                Scaffold(
                    topBar = {
                        Header(
                            img = state.user?.img,
                            show = state.showEnd,
                            onShow = state.onShowEnd,
                            deslogar = {  }
                        )
                    },
                    content = { innerPadding ->
                        Body(
                            pad = innerPadding,
                            nav = { navController.navigate(it) },
                            program = state.program,
                            status = state.status,
                            music = state.track,
                        )
                    },
                    bottomBar = {
                        NavBotton(
                            musica = state.track,
                            gatilho = state.trigger,
                            tocar = { track -> viewModel.playTrack(track) },
                            onGatilho = { state.onTrigger(it) }

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
                    LoadingScreen()
                    viewModel.loading()
                }
            }
            "ERROR" -> {
                ErrorScreen()
                LaunchedEffect(key1 = Unit) {
                    delay(1500)
                    navController.navigate(Destination.Login.route)
                }
            }
        }
    }
}


