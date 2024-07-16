package com.example.oiidar.conectionApi

import android.content.Context
import android.util.Log
import com.example.oiidar.contantes.CLIENT_ID
import com.example.oiidar.contantes.REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

object Spotify {
    var spotifyAppRemote: SpotifyAppRemote? = null
     fun Conectar(context: Context){
         val connectionParams = ConnectionParams.Builder(CLIENT_ID)
             .setRedirectUri(REDIRECT_URI)
             .showAuthView(true)
             .build()
         SpotifyAppRemote.connect(
             context, connectionParams, object : Connector.ConnectionListener {
                 override fun onConnected(appRemote: SpotifyAppRemote) {
                     spotifyAppRemote = appRemote
                     Log.d("MainActivity", "Connected! Yay!")
                 }
                 override fun onFailure(throwable: Throwable) {
                     Log.e("MainActivity", throwable.message, throwable)
                     // Something went wrong when attempting to connect! Handle errors here
                 }
             }
         )

    }

    fun desconectar(){
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
            Log.d("MainActivity", "Desconnected! Yay!")
        }
    }

     fun tocar(uri: String) {
        spotifyAppRemote?.let {
            it.playerApi.play(uri)
            Log.d("MainActivity", "em Spotify.tocar $uri")
        }
     }


    fun adionarFila(uri: String){
        spotifyAppRemote?.let {
            val playlistURI = "spotify:playlist:3cEYpjA9oz9GiPac4AsH4n"
            it.playerApi.queue(uri)
            //it.contentApi.getRecommendedContentItems(uri)
            // Subscribe to PlayerState
        }
    }
}


/*
D  Message from Spotify: [36,15,68,{},[],{
    "context_uri":"spotify:playlist:70xjTJhIMWJOlJUzRjoAFQ",
    "context_title":"E4 - 10:40",
    "track":{
        "artist":{
            "name":"Black Pumas","type":"artist",
            "uri":"spotify:artist:6eU0jV2eEZ8XTM7EmlguK6"
        },
        "artists":[
            {
            "name":"Black Pumas",
            "type":"artist",
            "uri":"spotify:artist:6eU0jV2eEZ8XTM7EmlguK6"
            }
        ],
        "album":{
            "name":"Black Pumas",
            "type":"album",
            "uri":"spotify:album:4KJGypBUe7ANibtri1msUe"
        },
        "saved":false,
        "duration_ms":246586,
        "name":"Colors",
        "uri":"spotify:track:6d4FWjx72iuRWzn1HwywLK",
        "uid":"67b27fe7958d25b9",
        "image_id":"spotify:image:ab67616d0000b273b78008fa0713ff7e5445566b",
        "is_episode":false,
        "is_podcast":false},
        "is_paused":false,
        "is_paused_bool":false,
        "playback_speed":1.0,
        "playback_position":8981,
        "playback_options":{
            "shuffle":false,
            "repeat":2
    },
    "playback_restrictions":{
        "can_skip_next":true,
        "can_skip_prev":true,
        "can_repeat_track":true,
        "can_repeat_context":true,
        "can_toggle_shuffle":true,
        "can_seek":true
    }
}]
 */










