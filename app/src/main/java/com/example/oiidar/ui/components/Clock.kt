package com.example.oiidar.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.oiidar.model.Horas
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.text.*

@SuppressLint("DefaultLocale")
@Composable
fun Clock(
    modifier: Modifier = Modifier
) {
    fun nowHoras(): Horas {
        val clock = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return Horas(clock.hour.toLong(), clock.minute.toLong(), clock.second.toLong())
    }
    var horas by remember { mutableStateOf(nowHoras()) }
    Box(modifier.padding(32.dp)){
        LaunchedEffect(key1 = true) {
            while(true) {
                delay(1000)
                horas = nowHoras()
            }
        }
        Text(
            text = String.format("%02d:%02d:%02d", horas.hour, horas.minute, horas.second),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.displayLarge,
        )
    }

}
