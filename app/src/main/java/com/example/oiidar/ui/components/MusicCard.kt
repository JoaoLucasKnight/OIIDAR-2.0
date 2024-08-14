package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.oiidar.database.entities.TrackEntity

@Composable
fun MusicCard(
    music: TrackEntity

){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.size(width = 140.dp, height = 200.dp)
    ) {
        AsyncImage(
            model = music.img,
            contentDescription = "Picture of Music",
            modifier = Modifier.size(140.dp)
        )
        Text(
            text = music.name,
            modifier = Modifier.padding(8.dp, 16.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2
        )
    }
}
