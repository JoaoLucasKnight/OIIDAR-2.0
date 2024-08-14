package com.example.oiidar.model

import com.example.oiidar.convertType.toTrackEntity
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.TrackEntity

data class SpotifyPlaylist(
    val href: String,
    val id: String,
    val images: List<Images>,
    val name: String,
    val tracks: Tracks,
    val uri: String
){
    fun getListTrackItems() : List<TrackItems>{
        return tracks.items
    }
    fun getTrackEntity(track: TrackItems, id: String) : TrackEntity {
        return track.track.toTrackEntity(id)
    }
    fun toPlaylistEntity(userId: String,duration: Long) : PlaylistEntity {
        return PlaylistEntity(
            userId = userId,
            id = id,
            img = images.first().url ,
            name = name,
            uri = uri,
            duration = duration
        )
    }
}
