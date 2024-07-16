package com.example.oiidar.model

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
        for (track in listaDeTracks) soma += track.track.durationMs
        return soma
    }
}

