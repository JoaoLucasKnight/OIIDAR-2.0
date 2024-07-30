package com.example.oiidar.ui.screens

import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.navigation.Destination
import com.example.oiidar.ui.viewModel.HomeVM
import com.example.oiidar.ui.components.Body
import com.example.oiidar.ui.components.Header
import com.example.oiidar.ui.components.NavBotton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.sql.Time


@Composable
@ExperimentalMaterial3Api
fun Home(
    navController : NavController,
    deslogar : () -> Unit
){
    val viewModel: HomeVM = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    Column (
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(0.dp, 32.dp, 0.dp, 0.dp))
    {
        if(state.user != null) {
            Scaffold(
                topBar = {
                    Header(
                        img = state.user?.img,
                        show = state.showSair,
                        onShow = state.onShowSair,
                        deslogar = { deslogar()}
                    )
                },
                content = { innerPadding ->
                    Body(
                        pad = innerPadding,
                        nav = navController,
                        programa = state.programa,
                        status = state.status,
                        musica = state.musica,
                    )
                },
                bottomBar = {
                    NavBotton(
                        musica = state.musica,
                        gatilho = state.gatilho,
                        tocar = { track -> viewModel.playTrack(track) },
                        onGatilho = { state.onGatilho(it) }

                    )
                }
            )
            LaunchedEffect(key1 = state.status) {
                while(state.status){
                    delay(viewModel.trackNow())
                }
            }
        }else {
            Surface{
                Carregamento()
                viewModel.loadInit()
            }
        }

    }
}


