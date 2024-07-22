package com.example.oiidar.convertType

import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Album
import com.example.oiidar.model.Horas
import com.example.oiidar.model.Images
import com.example.oiidar.model.SpotifyPlaylist
import com.example.oiidar.model.SpotifyUser
import com.example.oiidar.model.Track
import com.example.oiidar.model.Tracks
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ConvertKtTest{
    val img = Images(
        //height = 300,
        url = "url teste",
        //width = 300
    )
    @Test
    fun convertUserEntity()= runTest{
        val spotifyUser = SpotifyUser(
            displayName = "Nf3",
            href = "https://api.spotify.com/v1/users/123456789",
            images = listOf(img),
            id = "kirajota1",
            type = "user",
            uri = "spotify:user:123456789"
        )

        val entityUser = UserEntity(
            nameId = "kirajota1",
            img = "url teste",
            status = true
        )

        val userEntity = spotifyUser.toUser()

        assertEquals(userEntity, entityUser)
    }
    @Test
    fun convertSpotifyUser()= runTest{
        val spotifyPlaylist = SpotifyPlaylist(
            href = "href teste",
            id = "id teste",
            images = listOf(img),
            name = "name teste",
            tracks = mockk<Tracks>(),
            uri = "uri teste"
        )
        val entityPlaylist = PlaylistEntity(
             "kirajota1","id teste","url teste","name teste",
            "uri teste", 1000
        )

        val playlistEntity = spotifyPlaylist.toPlaylist("kirajota1", 1000)

        assertEquals(playlistEntity, entityPlaylist)

    }
    @Test
    fun convertTrack()= runTest{
        val album = Album(images = listOf(img),)

        val track = Track(1000, album, "id teste", "name teste",
        "type teste", "uri teste")

        val entityTrack = TrackEntity("kirajota1", "id teste",
            "name teste", "url teste",  "uri teste", 1000
        )


        val trackEntity = track.toTrackEntity("kirajota1")

        assertEquals(trackEntity, entityTrack)
    }
    @Test
    fun convertHoras()= runTest{
        val horas = Horas(23,59, 60)
        val result: Long = 86400000

        val ms = horas.toMs()
        assertEquals(ms, result)
    }

}
