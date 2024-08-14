package com.example.oiidar.repositories


import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Album
import com.example.oiidar.model.SpotifyPlaylist
import com.example.oiidar.model.Track
import com.example.oiidar.model.TrackItems
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var dao: Dao
    private lateinit var api: UserService
    private lateinit var playApi: PlaylistService
    private lateinit var repository: Repository

    private val user = UserEntity("nome", "email",true)
    private val program = ProgramaEntity("nome")
    private val playlist = PlaylistEntity(
        "nome", "link", "img", "desc", "uri", 1000)
    private val listPlaylist = listOf(playlist,playlist,playlist)
    private val trackEntity =
        TrackEntity("idPlaylist", "id", "name", "img","uri", 1000)
    private val listTracks = listOf(trackEntity,trackEntity)
    private val spotifyPlaylist = mockk<SpotifyPlaylist>()
    private val track = Track(1000,mockk<Album>(),"img","name","type", "uri")
    private val trackItems = TrackItems(track)
    private val listTrackItems = listOf(trackItems,trackItems,trackItems)
@Before
fun setUp() {
    dao = mockk<Dao>()
    api = mockk<UserService>()
    playApi = mockk<PlaylistService>()
    repository = Repository(dao, api, playApi)
}
    @Test
    fun test_userLogIn()= runTest {
        coEvery { dao.getUserLogIn() } returns user

        repository.userLogIn()

        coVerify { dao.getUserLogIn() }
    }
    @Test
    fun test_checkUserSave()= runTest {
        coEvery { dao.getUser(user.nameId) } returns user

        val result = repository.checkUserSave(user)

        coVerify { dao.getUser(user.nameId) }
        assertEquals(result, user)
    }
    @Test
    fun test_checkUserSave_Null()= runTest {
        coEvery { dao.getUser(user.nameId) } returns null

        val result = repository.checkUserSave(user)

        coVerify { dao.getUser(user.nameId) }
        assertEquals(result, null)
    }
    @Test
    fun test_updateStatusUser()= runTest {
        coEvery { dao.updateStatus(any(),user.nameId) } just Runs

        repository.updateStatusUser(false,user)

        coVerify { dao.updateStatus(false,user.nameId) }
    }
    @Test
    fun test_getProgram()= runTest {
        coEvery { dao.getProgram(any()) } returns mockk<ProgramaEntity>()

        repository.getProgram(user)

        coVerify { dao.getProgram(user.nameId) }
    }
    @Test
    fun test_updateStartProgram()= runTest {

        coEvery { dao.updateStartDuration(any(),user.nameId) } just Runs
        coEvery { dao.getListPlaylists(user.nameId) } returns listPlaylist
        coEvery { dao.getProgram(any()) } returns ProgramaEntity("nome", 1000, 0)
        coEvery { dao.updateFinishDuration(any(),user.nameId) } just Runs

        repository.updateStartProgram(1000, user)

        coVerifySequence {
            dao.updateStartDuration(1000,user.nameId)
            dao.getListPlaylists(user.nameId)
            dao.getProgram(user.nameId)
            dao.updateFinishDuration(4000,user.nameId)
        }

    }
    @Test
    fun test_updateProgram()= runTest {
        coEvery { dao.getListPlaylists(user.nameId) } returns listPlaylist
        coEvery { dao.getProgram(any()) } returns program
        coEvery { dao.updateFinishDuration(any(),user.nameId) } just Runs

        repository.updateProgram(user)

        coVerifySequence {
            dao.getListPlaylists(user.nameId)
            dao.getProgram(user.nameId)
            dao.updateFinishDuration(3000,user.nameId)
        }

    }
    @Test
    fun test_saveUser()= runTest {
        coEvery { dao.getListPlaylists(user.nameId) } returns listPlaylist
        repository.getListPlaylists(user)
        coVerify { dao.getListPlaylists(user.nameId) }
    }
    @Test
    fun test_getTracksUser()= runTest {
        coEvery { dao.getListPlaylists(any()) } returns listPlaylist
        coEvery { dao.getTracksPlaylist(any()) } returns listTracks

        val result = repository.getTracksUser(user)

        coVerifySequence {
            dao.getListPlaylists(user.nameId)
            dao.getTracksPlaylist(any())
            dao.getTracksPlaylist(any())
            dao.getTracksPlaylist(any())
        }
        assertEquals(result.size, 6)
    }
    @Test
    fun test_searchAndSave()= runTest {
        coEvery { playApi.getPlaylist(any()) } returns spotifyPlaylist
        coEvery { spotifyPlaylist.toPlaylistEntity(any(),any()) } returns playlist
        coEvery { spotifyPlaylist.getListTrackItems() } returns listTrackItems // 3 with 1 Track
        coEvery { spotifyPlaylist.getTrackEntity(trackItems,any()) } returns trackEntity
        coEvery { dao.saveTrack(any()) } just Runs
        coEvery { dao.savePlaylist(any()) } just Runs

        repository.searchAndSave("idPlaylist", user)

        coVerifySequence {
            playApi.getPlaylist("idPlaylist")
            spotifyPlaylist.getListTrackItems()
            spotifyPlaylist.getTrackEntity(any(), "idPlaylist")
            dao.saveTrack(any())
            spotifyPlaylist.getTrackEntity(any(), "idPlaylist")
            dao.saveTrack(any())
            spotifyPlaylist.getTrackEntity(any(), "idPlaylist")
            dao.saveTrack(any())
            spotifyPlaylist.toPlaylistEntity(user.nameId, 3000)
            dao.savePlaylist(any())
        }

    }
    @Test
    fun test_removePlaylistAndTrack()= runTest {
        coEvery { dao.getTracksPlaylist(any()) } returns listTracks
        coEvery { dao.deleteTrack(any()) } just Runs
        coEvery { dao.getPlaylist(any()) } returns playlist
        coEvery { dao.deletePlaylist(any()) } just Runs

        repository.removePlaylistAndTrack("idPlaylist")

        coVerifySequence {
            dao.getTracksPlaylist("idPlaylist")
            dao.deleteTrack(any())
            dao.deleteTrack(any())

            dao.getPlaylist("idPlaylist")
            dao.deletePlaylist(playlist)
        }
    }
    @Test
    fun test_saveUserAndProgram()= runTest {
        coEvery { dao.saveUser(user) } just Runs
        coEvery { dao.saveProgram(ProgramaEntity(user.nameId)) } just Runs
        coEvery { dao.updateStatus(true,user.nameId) } just Runs

        repository.saveUserAndProgram(user)

        coVerifySequence{
            dao.saveUser(user)
            dao.saveProgram(ProgramaEntity(user.nameId))
            dao.updateStatus(true,user.nameId)
        }
    }
}