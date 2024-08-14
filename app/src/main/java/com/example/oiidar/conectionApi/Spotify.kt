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
        spotifyAppRemote?.playerApi?.queue(uri)
    }
}











