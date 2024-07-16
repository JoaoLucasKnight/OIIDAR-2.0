package com.example.oiidar.model

import com.example.oiidar.database.entities.TrackEntity


data class Track(
    val duration_ms: Long,
    val album: Album,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

fun Track.toTrackEntity(idPlayist: String): TrackEntity {
    return TrackEntity(
        playlistId = idPlayist,
        id = id,
        name = name,
        img = album.images.first().url,
        duration = duration_ms,
        uri = uri
    )
}


