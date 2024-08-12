package com.example.oiidar.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.rules.TestRule

class HomeViewModelTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var repository: Repository
    private lateinit var vm: HomeViewModel

    private val track =
        TrackEntity("play","id", "name", "img", "uri", 200000)
    private val track1 =
        TrackEntity("play1","id1", "name1", "img1", "uri1", 400000)
    private val track2 =
        TrackEntity("play2","id2", "name2", "img2", "uri2", 600000)
    private val list = listOf(track, track1, track2)
    @Before
    fun setup() {
        repository = mockk()
        vm = HomeViewModel(repository)
    }
    @Test
    fun test_checkAndUpdateProgramStatus()= runBlocking {
        //when
        val program = ProgramaEntity("", 10800000, 18592912)
        val nowValues: List<Long> = listOf(10800000, 18592912, 15151515)
        val repository = mockk<Repository>()
        val vm = HomeViewModel(repository)
        for (now in nowValues){
            vm.checkAndUpdateProgramStatus(program, now)
            assertEquals(vm.uiState.value.status, true)
        }
    }
    @Test
    fun test_discovery_nowEst()= runBlocking {
        val program = ProgramaEntity("", 10800000, 12000000)
        val delay = vm.discoverTrackPlaying(program, list, 13000000)

        assertEquals(vm.uiState.value.track, null)
        assertEquals(delay, 0)
    }
    @Test
    fun test_discovery_nowEss()= runBlocking {
        val program = ProgramaEntity("", 10800000, 12000000)
        val delay = vm.discoverTrackPlaying(program,  list, 10700000)

        assertEquals(vm.uiState.value.track, null)
        assertEquals(delay, 0)
    }
    @Test
    fun test_next_track()= runBlocking {
        val program = ProgramaEntity("", 10800000, 12000000)
        vm.discoverTrackPlaying(program, list, 10800000)

        val delay = vm.nextTrack(listTrack = list)

        assertEquals(vm.uiState.value.track, track1)
        assertEquals(delay, 400000)
    }
    @Test
    fun test_trackNow_lastTrack()= runBlocking {
        val program = ProgramaEntity("", 10800000, 12000000)
        vm.discoverTrackPlaying(program, list, 11900000)

        val delay = vm.nextTrack(listTrack = list)

        assertEquals(vm.uiState.value.track, null)
        assertEquals(delay, 0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_loading()= runBlocking{
        Dispatchers.setMain(Dispatchers.IO)
        val repos = mockk<Repository>()
        val viewModel = HomeViewModel(repos)
        val user= UserEntity("id", "img", true)
        val program = ProgramaEntity("id", 10800000, 12000000)
        val list = mockk<List<TrackEntity>>(relaxed = true)

        coEvery { repos.userLogIn() } returns user
        coEvery { repos.getProgram(any()) } returns program
        coEvery { repos.getTracksUser(any()) } returns list

        viewModel.loadUser()
        viewModel.loading()

        coVerifySequence {
            repos.userLogIn()
            repos.getProgram(user)
            repos.getTracksUser(user)
            repos.getProgram(user)
        }
        assertEquals(viewModel.uiState.value.user, user)
        assertEquals(viewModel.uiState.value.program, program)
        assertEquals(viewModel.uiState.value.tracks, list)
        Dispatchers.resetMain()
    }
}