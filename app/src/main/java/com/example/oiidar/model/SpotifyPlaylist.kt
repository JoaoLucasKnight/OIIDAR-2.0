package com.example.oiidar.model

data class SpotifyPlaylist(
    val href: String,
    val id: String,
    val images: List<Images>,
    val name: String,
    val tracks: Tracks,
    val uri: String
)
