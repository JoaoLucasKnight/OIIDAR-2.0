package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.ui.text.AppStrings
import com.example.oiidar.ui.uiStates.HomeState


@Composable
@ExperimentalMaterial3Api
fun NavBottom(
    play: (TrackEntity?) -> Unit,
    state: HomeState
){
    val music = state.track
    val trigger = state.trigger
    val onTrigger = state.onTrigger
    Row (
        Modifier.fillMaxWidth().height(80.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically)
    {
        Button(onClick = { onTrigger(true) }) {
            Text(text = AppStrings.NAVBOTTOM_BTT_NEXT)
        }
    }
    LaunchedEffect(key1 = music) {
        if(trigger) {
            play(music)
            onTrigger(false)
        }
    }
}


