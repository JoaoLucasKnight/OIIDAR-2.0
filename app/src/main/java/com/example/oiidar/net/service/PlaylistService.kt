package com.example.oiidar.net.service

import android.util.Log
import com.example.oiidar.model.SpotifyPlaylist
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaylistService {
    @GET(" playlists/{id}")
    suspend fun getPlaylist(@Path("id") id: String): SpotifyPlaylist
}

//6gJRdUeKRA9b7bAJfsA9qI
//?fields=href,id,images,name,tracks(next,offset,previous,total,items.track(duration_ms,id,name,type,uri)),uri