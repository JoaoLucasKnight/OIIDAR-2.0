package com.example.oiidar.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Observer
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.repositories.Repository
import com.example.oiidar.ui.uiStates.HomeScreenUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.rules.TestRule

class HomeVMTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var repository: Repository
    lateinit var vm: HomeVM
    val track =
        TrackEntity("play","id", "name", "img", "uri", 200000)
    val track1 =
        TrackEntity("play1","id1", "name1", "img1", "uri1", 400000)
    val track2 =
        TrackEntity("play2","id2", "name2", "img2", "uri2", 600000)
    val list = listOf(track, track1, track2)
    @Before
    fun setup() {
        repository = mockk()
        vm = HomeVM(repository)

    }
    @Test
    fun test_checkAndUpdateProgramStatus()= runBlocking {
        //when
        val program = ProgramaEntity("", 10800000, 18592912)
        val nowValues: List<Long> = listOf(10800000, 18592912, 15151515)
        val repository = mockk<Repository>()
        val vm = HomeVM(repository)
        for (now in nowValues){
            vm.checkAndUpdateProgramStatus(program, now)
            assertEquals(vm.uiState.value.status, true)
        }
    }
    @Test
    fun test_trackNow_trackNull()= runBlocking {
        val trackNull= null
        val program = ProgramaEntity("", 10800000, 12000000)

        val delay = vm.trackNow(program, trackNull, list, 10800000)

        assertEquals(vm.uiState.value.musica, track)
        assertEquals(delay, 200000)
    }
    @Test
    fun test_trackNow_trackNull_programEst()= runBlocking {
        val trackNull= null
        val program = ProgramaEntity("", 10800000, 12000000)

        val delay = vm.trackNow(program, trackNull, list, 13000000)

        assertEquals(vm.uiState.value.musica, null)
        assertEquals(delay, 0)
    }
    @Test
    fun test_trackNow_trackNull_programEss()= runBlocking {
        val trackNull= null
        val program = ProgramaEntity("", 10800000, 12000000)

        val delay = vm.trackNow(program, trackNull, list, 10700000)

        assertEquals(vm.uiState.value.musica, null)
        assertEquals(delay, 0)
    }
    @Test
    fun test_trackNow_track()= runBlocking {
        val program = ProgramaEntity("", 10800000, 12000000)

        val delay = vm.trackNow(program, track1, list, 10800000)

        assertEquals(vm.uiState.value.musica, track2)
        assertEquals(delay, 600000)
    }
    @Test
    fun test_trackNow_lastTrack()= runBlocking {
        val program = ProgramaEntity("", 10800000, 12000000)

        val delay = vm.trackNow(program, track2, list, 10800000)

        assertEquals(vm.uiState.value.musica, null)
        assertEquals(delay, 0)
    }
}