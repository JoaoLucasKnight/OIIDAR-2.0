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
    viewModel: HomeVM,
    navController : NavController,
    deslogar : () -> Unit
){
    val state by viewModel.uiState.collectAsState()

    Column (
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(0.dp, 32.dp, 0.dp, 0.dp))
    {
        state.atualiza()
        if(state.carregado) {
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
                        // passar prograna
                        programa = state.programa!!,
                        inicio = state.msToHoras(state.programa?.startTime).toString(),
                        fim = state.msToHoras(state.programa?.finishTime).toString(),
                        del = state.del,
                        status = state.status,
                        musica = state.musica,
                        onStatus = { state.onStatus(it) },
                        onMusica = { state.onMusica() },
                        proximo = { state.proxima(it) },
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
        }else {
            Surface{
                Carregamento()
            }
        }

    }
}


