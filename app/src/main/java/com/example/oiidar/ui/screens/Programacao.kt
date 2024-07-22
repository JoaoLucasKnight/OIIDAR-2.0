package com.example.oiidar.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oiidar.convertType.toMs
import com.example.oiidar.ui.viewModel.ProgramacaoVM
import com.example.oiidar.ui.components.Header
import com.example.oiidar.ui.components.Playlists
import com.example.oiidar.ui.dialogs.TimerEditorDialog
import com.example.oiidar.ui.theme.OIIDARTheme

import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterial3Api
fun Programacao(
    viewModel: ProgramacaoVM,
    deslogar: () -> Unit
){
    val state by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(0.dp, 32.dp, 0.dp, 0.dp),
        topBar = {
            Header(
                img = state.user?.img,
                show = state.showSair,
                onShow = state.onShowSair,
                deslogar = { deslogar() }
            )
        },
        content = { pad ->
            Column(modifier = Modifier.padding(pad).padding(16.dp, 0.dp)) {
                OutlinedTextField (
                    value = state.url,
                    onValueChange = {state.onUrl(it)},
                    modifier = Modifier.fillMaxWidth().padding(0.dp, 32.dp),
                    label = {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Url da playlist")
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.searchAndSave(state.url)
                            focusManager.clearFocus()
                        }
                    )
                )
                Text(
                    text = "Editar Programação",
                    style = MaterialTheme.typography.headlineSmall
                )
                Playlists(lista = state.playlitsts,
                    conversor = { somaTotal: Long ->
                                state.conversorMs(somaTotal)
                    },
                    apagaPlaylist = { playlist ->
                        viewModel.removePlaylist(playlist)
                    }
                )
                Row (
                    modifier = Modifier.fillMaxWidth().padding(0.dp, 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "Editar Inicio:",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    OutlinedButton(
                        onClick = { state.onShowTimer(state.showTimer)}
                    ) {
                        Text(
                            text = state.msToHoras(state.programa?.startTime)
                                .toString(),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                }
                Box (modifier = Modifier.fillMaxWidth().padding(16.dp, 16.dp)){
                    Text(
                        text = "Sua programação irá acabar ás: ${
                            state.conversorMs(state.programa?.finishTime)}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    )
    if (state.showTimer) {

        TimerEditorDialog(
            onDismissRequest = { state.onShowTimer(state.showTimer) },
            time = state.msToHoras(state.programa?.startTime),
            salvar = { horas ->
                viewModel.updateStartProgram(horas.toMs())
                state.onShowTimer(state.showTimer)
            }
        )
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun ProgramacaoPreview(){
    OIIDARTheme {
        Surface {
        }
    }
}