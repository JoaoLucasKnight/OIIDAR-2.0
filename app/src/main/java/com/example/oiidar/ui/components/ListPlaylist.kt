package com.example.oiidar.ui.components

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
import com.example.oiidar.convertType.toHoras
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.ui.theme.OIIDARTheme


@Composable
fun Playlists(
    list: List<PlaylistEntity>,
    deletePlaylist: (String) -> Unit
){
    LazyColumn(
        Modifier.padding(16.dp)
    ){
        for(playlist in list){
            item{
                ListItem(
                    headlineContent = { Text(playlist.name) },
                    overlineContent = { Text("PLAYLIST") },
                    supportingContent = { Text(toHoras(playlist.duration).toString()) },
                    leadingContent = {
                        AsyncImage(
                            model = playlist.img,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                            )
                    },
                    trailingContent = {
                        IconButton(onClick = { deletePlaylist(playlist.id) }) {
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