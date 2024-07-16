package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.model.Horas
import com.example.oiidar.model.ToMs
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun Relogio(
    programa: ProgramaEntity,
    status: Boolean,
    onStatus: (Boolean) -> Unit
){
    fun horasAtual(): Horas {
        val relogio = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return Horas(relogio.hour.toLong(), relogio.minute.toLong(), relogio.second.toLong())
    }

    var horas by remember { mutableStateOf(horasAtual()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
    ){
        LaunchedEffect(key1 = true) {
            while (true) {
                delay(1000)
                // atualiza a hora
                horas = horasAtual()
                if(programa.tempoInicio < horas.ToMs() &&
                    horas.ToMs() < programa.tempoFinal ){
                    if(status == false){
                        onStatus(true)
                    }
                } else{
                    if(status == true){
                        onStatus(false)
                    }
                }
            }
        }

        Text(
            text = String.format("%02d:%02d:%02d", horas.horas, horas.minutos, horas.segundos),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.displayLarge,
        )
    }
}
