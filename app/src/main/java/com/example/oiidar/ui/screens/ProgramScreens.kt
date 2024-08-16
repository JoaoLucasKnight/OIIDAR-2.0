package com.example.oiidar.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.oiidar.convertType.toHoras
import com.example.oiidar.convertType.toMs
import com.example.oiidar.model.Horas
import com.example.oiidar.ui.components.Header
import com.example.oiidar.ui.components.Playlists
import com.example.oiidar.ui.components.TextTitle
import com.example.oiidar.ui.dialogs.TimerEditDialog
import com.example.oiidar.ui.text.AppStrings
import com.example.oiidar.ui.viewModel.ProgramViewModel
import kotlinx.coroutines.delay

@Composable
@ExperimentalMaterial3Api
fun ProgramScreens(
    nav: (String) -> Unit,
    logOut: () -> Unit
) {
    val viewModel: ProgramViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    val fatherModifier = Modifier.fillMaxWidth().padding(24.dp)
    when(state.loading){
        "LOADING" ->{
            Surface{
                LoadingScreen()
                viewModel.loading()
            }
        }
        "ERROR" ->{
            Surface{
                ErrorScreen()
                LaunchedEffect(key1 = Unit) {
                    delay(1500)
                    logOut()
                }
            }
        }
        "LOAD" ->{
            val startHour = state.program?.let { toHoras(it.startTime) }
                ?: Horas()
            Scaffold(
                topBar = {
                    Header(
                        img = state.user?.img,
                        show = state.showEnd,
                        onShow = state.onShowEnd,
                        logOut = {
                            logOut()
                        }
                    )
                },
                content = { pad ->
                    Column(modifier = Modifier.padding(pad))
                    {
                        OutlinedTextField (
                            value = state.url,
                            onValueChange = {state.onUrl(it)},
                            modifier = fatherModifier,
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
                            keyboardActions = KeyboardActions(onSearch = {
                                    viewModel.searchAndSave(state.url)
                                    focusManager.clearFocus()
                                }
                            )
                        )
                        Column(fatherModifier){
                            // add scroll over here
                            TextTitle(text = AppStrings.TITLE_PLAYLIST)
                            Playlists(
                                list = state.listPlaylist,
                                deletePlaylist = { playlist -> viewModel.removePlaylist(playlist) }
                            )
                        }
                    }
                },
                bottomBar = {
                    Row (
                        modifier = Modifier.height(92.dp).then(fatherModifier),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = AppStrings.TITLE_E_STAR,
                            Modifier.padding(top =8.dp),
                            style = MaterialTheme.typography.headlineMedium)
                        OutlinedButton(onClick = { state.onShowTimer(true)}
                        ) {
                            Text(
                                text = startHour.toString(),
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                    }
                }
            )
            if (state.showTimer) {
                TimerEditDialog(
                    onDismissRequest = { state.onShowTimer(false) },
                    time = startHour,
                    salvar = { horas ->
                        viewModel.updateStartProgram(horas.toMs())
                        state.onShowTimer(false)
                    }
                )
            }
        }
    }
}
