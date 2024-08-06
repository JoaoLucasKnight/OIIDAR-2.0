package com.example.oiidar.ui.viewModel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
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

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.IO)
        repository = mockk(relaxed = true)
        vm = ProgramViewModel(repository)
    }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun testSearchAndSave_user()= runBlocking {

        vm.searchAndSave("test id", user)

        coVerify{
            repository.searchAndSave(any(),any())
            repository.updateProgram(any())
        }
        assertEquals( vm.uiState.value.loading, "LOAD")
    }

    @Test
    fun testSearchAndSave_userNull()= runBlocking {
        vm.searchAndSave("test id",null)
        coVerify(exactly = 0) {
            repository.searchAndSave(any(),any())
            repository.updateProgram(any())
        }
        assertEquals( vm.uiState.value.loading, "ERROR")
    }

    @Test
    fun testRemovePlaylist_user()= runBlocking {

        vm.removePlaylist("idPlaylist", user)

        coVerify {
            repository.removePlaylistAndTrack(any())
            repository.updateProgram(any())
        }
        assertEquals(vm.uiState.value.loading, "LOAD")
    }

    @Test
    fun testRemovePlaylist_userNull()= runBlocking {
        vm.removePlaylist("idPlaylist", null)

        coVerify(exactly = 0) {
            repository.removePlaylistAndTrack(any())
            repository.updateProgram(any())
        }
        assertEquals(vm.uiState.value.loading, "ERROR")
    }

    @Test
    fun testUpdateStarProgram_user()= runBlocking {
        vm.updateStartProgram(123456, user)

        coVerify {
            repository.updateStartProgram(any(),user.nameId)
        }
        assertEquals(vm.uiState.value.loading, "LOAD")
    }

}


























