package com.example.oiidar.convertType

import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas
import com.example.oiidar.model.SpotifyPlaylist
import com.example.oiidar.model.SpotifyUser
import com.example.oiidar.model.Track
import com.example.oiidar.model.Tracks

fun SpotifyUser.toUser(): UserEntity {
    return UserEntity(
        nameId = id,
        img = images.last().url,
        status = true
    )
}
fun SpotifyPlaylist.toPlaylist(userId: String, duration: Long): PlaylistEntity {
    return PlaylistEntity(
        userId = userId,
        id = id,
        img = images.first().url,
        name = name,
        uri = uri,
        duration = duration
    )
}
fun Track.toTrackEntity(idPlayist: String): TrackEntity {
    return TrackEntity(
        playlistId = idPlayist,
        id = id,
        name = name,
        img = album.images.first().url,
        duration = durationMs,
        uri = uri
    )
}
fun Horas.ToMs(): Long{
    return horas * 3600000 + minutos * 60000 + segundos * 1000
}

