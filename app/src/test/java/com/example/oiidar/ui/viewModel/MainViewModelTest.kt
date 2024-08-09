package com.example.oiidar.ui.viewModel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
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


@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var repository: Repository
    private lateinit var viewModel: MainViewModel

    private val user = UserEntity("nome", "email",true)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.IO)
        repository = mockk()
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun checkSaveOrSave_False()= runTest {
        val userNull: UserEntity? = null
        coEvery { repository.getSpotifyUser() } returns user
        coEvery { repository.checkUserSave(any()) } returns userNull
        coEvery { repository.saveUserAndProgram(any()) } just Runs

        viewModel.checkSaveOrSave()

        coVerifySequence{
            repository.getSpotifyUser()
            repository.checkUserSave(any())
            repository.saveUserAndProgram(any())
        }
        assertEquals(viewModel.user.value, user)
    }
    @Test
    fun checkSaveOrSave_True()= runTest {
        coEvery { repository.getSpotifyUser() } returns user
        coEvery { repository.checkUserSave(any()) } returns user
        coEvery { repository.updateStatusUser(any(),any()) } just Runs

        viewModel.checkSaveOrSave()

        coVerifySequence{
            repository.getSpotifyUser()
            repository.checkUserSave(any())
            repository.updateStatusUser(any(),any())
        }
        assertEquals(viewModel.user.value, user)
    }
    @Test
    fun checkSaveOrSave_Exception()= runTest {
        coEvery { repository.getSpotifyUser() } throws Exception("Requisição falhou")

        viewModel.checkSaveOrSave()

        coVerifySequence{
            repository.getSpotifyUser()
        }
        assertEquals(viewModel.user.value, null)
    }
}