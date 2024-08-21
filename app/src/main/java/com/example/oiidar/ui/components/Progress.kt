package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun Progress (timer: Long){
    var progress by remember { mutableFloatStateOf(0f) }

    Box (Modifier.padding(top = 16.dp)){
            LinearProgressIndicator(
                progress = { progress },
                Modifier.fillMaxWidth()
            )
    }
    LaunchedEffect(key1 = timer) {
        val sec = timer/ 100
        for (i in 1..100) {
            progress = i.toFloat() / 100
            delay(sec)
        }
    }
}

