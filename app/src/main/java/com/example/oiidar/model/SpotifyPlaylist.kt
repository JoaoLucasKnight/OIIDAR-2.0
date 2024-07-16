package com.example.oiidar.model

import android.util.Log
import com.example.oiidar.database.entities.PlaylistEntity

data class SpotifyPlaylist(
    val href: String,
    val id: String,
    val images: List<Images>,
    val name: String,
    val tracks: Tracks,
    val uri: String
) {
    fun SpotifyPlaylist.duracaoPlaylist():Long {
        val listaDeTracks = tracks.items
        var soma: Long = 0
        for (track in listaDeTracks) {
            soma += track.track.duration_ms
            Log.d("duracao", "duracao: ${track.track.duration_ms}")
            Log.d("soma", "soma: ${soma}")
        }
        return soma
    }
}
fun SpotifyPlaylist.toPlaylist(userId: String): PlaylistEntity {
    return PlaylistEntity(
        userId = userId,
        id = id,
        img = images.first().url,
        name = name,
        uri = uri,
        soma = duracaoPlaylist(),
    )
}
