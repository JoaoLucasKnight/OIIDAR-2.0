package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.ui.text.AppStrings

@Composable
fun Music(
    modifier: Modifier,
    status: Boolean,
    music: TrackEntity?
){
    Column(modifier) {
        TextTitle(text = AppStrings.MUSIC_TITLE)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(status){
                music?.let { MusicCard(music = it)
                }?: run { Text(AppStrings.MUSIC_DESC_NULL) }
            }else {
                Text(AppStrings.MUSIC_DESC_END)
            }
        }
    }

}