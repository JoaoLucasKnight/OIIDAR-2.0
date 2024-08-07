package com.example.oiidar.ui.viewModel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class ProgramViewModelTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var repository: Repository
    private lateinit var vm: ProgramViewModel

    private val user = UserEntity("test name", "test img", true)
    private val program = ProgramaEntity("test name", 123456789, 234567890)
    private val playlist = mockk<PlaylistEntity>()
    private val listPlaylist: List<PlaylistEntity> = listOf(playlist,playlist)

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.IO)
        repository = mockk()
        vm = ProgramViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchAndSave_user()= runTest {
        coEvery { repository.searchAndSave(any(),any()) } just Runs
        coEvery { repository.updateProgram(any()) } just Runs

        vm.searchAndSave("test id", user)

        coVerify {
            repository.searchAndSave(any(),any())
            repository.updateProgram(any())
        }
    }

    @Test
    fun testSearchAndSave_userNull()= runTest {
        coEvery { repository.searchAndSave(any(),any()) } just Runs
        coEvery { repository.updateProgram(any()) } just Runs

        vm.searchAndSave("test id",null)

        coVerify(exactly = 0) {
            repository.searchAndSave(any(),any())
            repository.updateProgram(any())
        }
        assertEquals( vm.uiState.value.loading, "ERROR")
    }

    @Test
    fun testRemovePlaylist_user()= runTest {

        coEvery { repository.removePlaylistAndTrack(any()) } just Runs
        coEvery { repository.updateProgram(any()) } just Runs

        vm.removePlaylist("idPlaylist", user)

        coVerifyOrder {
            repository.removePlaylistAndTrack(any())
            repository.updateProgram(any())
        }
    }

    @Test
    fun testRemovePlaylist_userNull()= runTest {
        coEvery { repository.removePlaylistAndTrack(any()) } just Runs
        coEvery { repository.updateProgram(any()) } just Runs

        vm.removePlaylist("idPlaylist", null)

        coVerify(exactly = 0) {
            repository.removePlaylistAndTrack(any())
            repository.updateProgram(any())
        }
        assertEquals(vm.uiState.value.loading, "ERROR")
    }

    @Test
    fun testUpdateStarProgram_user()= runTest {
        coEvery { repository.updateStartProgram(any(),any()) } just Runs
        coEvery { repository.getProgram(any()) } returns program
        coEvery { repository.getPlaylists(any()) } returns listPlaylist

        vm.updateStartProgram(123456, user)

        coVerify {
            repository.updateStartProgram(any(),any())
        }
    }

    @Test
    fun testUpdateStarProgram_userNull()= runTest {
        coEvery { repository.updateStartProgram(any(),any()) } just Runs

        vm.updateStartProgram(123456, null)

        coVerify(exactly = 0) {
            repository.updateStartProgram(any(),any())
        }
        assertEquals(vm.uiState.value.loading, "ERROR")
    }

    @Test
    fun testLoading_user()= runTest {
        coEvery { repository.userLogIn() } returns user
        coEvery { repository.getProgram(any()) } returns program
        coEvery { repository.getPlaylists(any()) } returns listPlaylist

        vm.loading(user)

        coVerifySequence {
            repository.getProgram(any())
            repository.getPlaylists(any())
        }
        assertEquals(vm.uiState.value.program, program)
        assertEquals(vm.uiState.value.listPlaylist, listPlaylist)
        assertEquals(vm.uiState.value.loading, "LOAD")
    }

    @Test
    fun testLoading_userNull()= runTest {
        coEvery { repository.userLogIn() } returns user
        coEvery { repository.getProgram(any()) } returns program
        coEvery { repository.getPlaylists(any()) } returns listPlaylist

        vm.loading(null)

        coVerifySequence {
            repository.userLogIn()
            repository.getProgram(any())
            repository.getPlaylists(any())
        }
        assertEquals(vm.uiState.value.user, user)
        assertEquals(vm.uiState.value.program, program)
        assertEquals(vm.uiState.value.listPlaylist, listPlaylist)
        assertEquals(vm.uiState.value.loading, "LOAD")
    }

    @Test
    fun testLoading_userException()= runTest {
        coEvery { repository.userLogIn() }  throws Exception("User null")
        coEvery { repository.getProgram(any()) } returns program
        coEvery { repository.getPlaylists(any()) } returns listPlaylist
        val list = emptyList<PlaylistEntity>()

        vm.loading(null)

        coVerifySequence {
            repository.userLogIn()
        }
        assertEquals(vm.uiState.value.user, null)
        assertEquals(vm.uiState.value.program, null)
        assertEquals(vm.uiState.value.listPlaylist, list)
        assertEquals(vm.uiState.value.loading, "ERROR")
    }
    @Test
    fun testLoading_programException()= runTest {
        coEvery { repository.userLogIn() }  returns user
        coEvery { repository.getProgram(any()) } throws Exception("program null")
        coEvery { repository.getPlaylists(any()) } returns listPlaylist
        val list = emptyList<PlaylistEntity>()

        vm.loading(null)

        coVerifySequence {
            repository.userLogIn()
            repository.getProgram(any())
        }
        assertEquals(vm.uiState.value.user, user)
        assertEquals(vm.uiState.value.program, null)
        assertEquals(vm.uiState.value.listPlaylist, list)
        assertEquals(vm.uiState.value.loading, "ERROR")
    }
}


























