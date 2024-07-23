package com.example.oiidar.repositories

import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class RepositoryTrackTest {
    private val dao = mockk<Dao>()
    private val api = mockk<UserService>()
    private val playApi = mockk<PlaylistService>()
    private val repository = Repository(dao,api,playApi)
    private val track = mockk<TrackEntity>()
    @Test
    fun testSaveTrack()=runTest {
        coEvery { dao.saveTrack(track) } returns Unit
        repository.saveTrack(track)
        coVerify { dao.saveTrack(track) }
    }
    @Test
    fun testDeleteTrack()=runTest {
        coEvery { dao.deleteTrack(track) } returns Unit
        repository.deleteTrack(track)
        coVerify { dao.deleteTrack(track) }
    }
    @Test
    fun testGetTracksPlaylist()=runTest {
        val id = "idPlaylistTest"
        coEvery { dao.getTracksPlaylist(id) } returns listOf(track)
        repository.getTracksPlaylist(id)
        coVerify { dao.getTracksPlaylist(id) }
    }
    @Test
    fun testGetTracksUser()=runTest {
        val idPlaylist = "idPlaylistTest"
        val idUser = "idUserTest"
        val playlist= PlaylistEntity(idUser,idPlaylist,"test","test","test",1000)
        val music = mockk<TrackEntity>()
        coEvery { dao.getPlaylists(idUser) } returns listOf(playlist,playlist,playlist)
        coEvery { dao.getTracksPlaylist(idPlaylist) } returns listOf(music,music, music)
        val list = repository.getTracksUser(idUser)
        assertEquals(9,list.size)
    }

}