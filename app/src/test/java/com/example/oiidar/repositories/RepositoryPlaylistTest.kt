package com.example.oiidar.repositories

import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RepositoryPlaylistTest {
    private val dao = mockk<Dao>()
    private val api = mockk<UserService>()
    private val playApi = mockk<PlaylistService>()
    private val repository = Repository(dao,api,playApi)
    private val playlist = mockk<PlaylistEntity>()

    @Test
    fun getPlaylist()= runTest{
        val idPlaylist = "test"
        coEvery { dao.getPlaylist(idPlaylist) } returns playlist
        repository.getPlaylist(idPlaylist)
        coVerify { dao.getPlaylist(idPlaylist) }
    }
    @Test
    fun getPlaylists()= runTest{
        val user =UserEntity("test","test",true)
        val playlists = listOf(playlist)
        coEvery { dao.getPlaylists(user.nameId) } returns playlists
        repository.getPlaylists(user.nameId)
        coVerify { dao.getPlaylists(user.nameId) }
    }
    @Test
    fun savePlaylist()= runTest{
        coEvery { dao.savePlaylist(playlist) } returns Unit
        repository.savePlaylist(playlist)
        coVerify { dao.savePlaylist(playlist) }
    }
    @Test
    fun deletePlaylist()= runTest{
        coEvery { dao.deletePlaylist(playlist) } returns Unit
        repository.deletePlaylist(playlist)
        coVerify { dao.deletePlaylist(playlist) }
    }
}