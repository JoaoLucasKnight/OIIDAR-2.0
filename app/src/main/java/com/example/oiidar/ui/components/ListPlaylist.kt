package com.example.oiidar.ui.components

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.ui.theme.OIIDARTheme
import com.example.oiidar.ui.viewModel.ProgramacaoVM


@Composable
fun Playlists(
    lista: List<PlaylistEntity>,
    conversor: (Long) -> String,
    apagaPlaylist: (String) -> Unit
){
    LazyColumn(
        Modifier.padding(16.dp)
    ){
        for(playlist in lista){
            item{
                ListItem(
                    headlineContent = { Text(playlist.name) },
                    overlineContent = { Text("PLAYLIST") },
                    supportingContent = { Text(conversor(playlist.duration)) },
                    leadingContent = {
                        AsyncImage(
                            model = playlist.img,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                            )
                    },
                    trailingContent = {
                        IconButton(onClick = { apagaPlaylist(playlist.id) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null
                            )
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
fun ListPlaylistPreview(){
    OIIDARTheme {
        Surface{}

    }
}