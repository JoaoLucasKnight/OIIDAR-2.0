package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oiidar.convertType.toHoras
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.navigation.Destination

@Composable
fun Body(
    pad: PaddingValues,
    nav: NavController,
    program: ProgramaEntity?,
    status: Boolean,
    musica: TrackEntity?,
){
    Column(
        modifier = Modifier
            .padding(pad)
            .padding(16.dp, 0.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        ){ Clock() }
        Column{
            Text(
                text = "Programação",
                style = MaterialTheme.typography.headlineSmall
            )
            Row(
                modifier = Modifier
                    .padding(0.dp, 32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                program?.let {
                    val startTime = program.startTime.toHoras(it.startTime)
                    val finishTime = program.finishTime.toHoras(it.finishTime)
                    Text(
                        text = "Incio: " + startTime.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Fim: "+ finishTime.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }
            OutlinedButton(
                onClick = { nav.navigate(Destination.Program.route) },
                modifier = Modifier
                    .padding(0.dp, 16.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Playlist")
            }
        }
        Column {
            Text(
                text = "Musicas",
                style = MaterialTheme.typography.headlineSmall,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(status){
                    musica?.let {
                        ElevaCard(
                            largura = 140,
                            altura = 200,
                            musica = musica
                        )
                    }?: run {
                        Text("No music")
                    }
                }else {
                    Text("No programming for the current time")
                }
            }
        }
    }
}