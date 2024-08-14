package com.example.oiidar.convertType

import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas
import com.example.oiidar.model.SpotifyUser
import com.example.oiidar.model.Track

fun SpotifyUser.toUser(): UserEntity {
    return UserEntity(
        nameId = id,
        img = images.last().url,
        status = true
    )
}

fun Track.toTrackEntity(idPlaylist: String): TrackEntity {
    return TrackEntity(
        playlistId = idPlaylist,
        id = id,
        name = name,
        img = album.images.first().url,
        duration = durationMs,
        uri = uri
    )
}
fun Horas.toMs(): Long{
    return hour * 3600000 + minute * 60000 + second * 1000
}
fun toHoras(ms: Long): Horas {
    val second = (ms  / 1000) % 60
    val minutes = (ms / (1000 * 60)) % 60
    val hours = (ms / (1000 * 60 * 60))
    return Horas(hours, minutes, second)
}

