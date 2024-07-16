package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.model.Horas
import com.example.oiidar.ui.theme.OIIDARTheme


@Composable
@ExperimentalMaterial3Api
fun NavBotton(
    musica: TrackEntity?,
    gatilho: Boolean,

    tocar: (TrackEntity?) -> Unit,
    onGatilho: (Boolean) -> Unit

){
    Row (
        Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically)
    {

        Button(onClick = { onGatilho(true) }) {// TODO Notificar horario
            Text(text = "Começar na Próxima")
        }
    }
    LaunchedEffect(key1 = musica) {
        if(gatilho) {
            tocar(musica)
            onGatilho(false)
        }
    }


}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun NavBottonPreview(){
    OIIDARTheme {
        Surface {

        }
    }
}

