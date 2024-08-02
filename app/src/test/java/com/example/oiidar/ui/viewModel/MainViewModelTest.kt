package com.example.oiidar.ui.viewModel


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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

import org.junit.Test


@ExperimentalCoroutinesApi
class MainViewModelTest {
    private val testDispatcher = Dispatchers.IO
    @Before
    fun setUp() { Dispatchers.setMain(testDispatcher) }
    @After
    fun tearDown() { Dispatchers.resetMain() }
    @Test
    fun checkSaveOrSave_False()= runTest {
        val user = UserEntity("nome", "email",true)
        val userNull: UserEntity? = null
        val repository = mockk<Repository>(relaxed = true)
        val viewModel = MainViewModel(repository)

        coEvery { repository.userLogIn() } returns user
        coEvery { repository.getSpotifyUser() } returns user
        coEvery { repository.checkUserSave(any()) } returns userNull
        coEvery { repository.saveUser(any()) } just Runs
        coEvery { repository.saveProgram(any()) } just Runs
        coEvery { repository.updateStatusUser(true,user.nameId) } just Runs


        viewModel.checkSaveOrSave()

        coVerifySequence{
            repository.userLogIn()
            repository.getSpotifyUser()
            repository.checkUserSave(any())
            repository.saveUser(any())
            repository.saveProgram(any())
            repository.updateStatusUser(true, "nome")
            repository.userLogIn()
        }

    }
    @Test
    fun checkSaveOrSave_True()= runTest {
        val user = UserEntity("nome", "email",true)
        val repository = mockk<Repository>(relaxed = true)
        val viewModel = MainViewModel(repository)

        coEvery { repository.userLogIn() } returns user
        coEvery { repository.getSpotifyUser() } returns user
        coEvery { repository.checkUserSave(any()) } returns user
        coEvery { repository.updateStatusUser(true, user.nameId) } just Runs


        viewModel.checkSaveOrSave()

        coVerifySequence{
            repository.userLogIn()
            repository.getSpotifyUser()
            repository.checkUserSave(any())
            repository.updateStatusUser(true, "nome")
            repository.userLogIn()
        }

    }
    @Test
    fun checkSaveOrSave_Exception()= runTest {
        val repository = mockk<Repository>(relaxed = true)
        val viewModel = MainViewModel(repository)

        coEvery { repository.userLogIn() } returns null
        coEvery { repository.getSpotifyUser() } throws Exception("Requisição falhou")
        coEvery { repository.checkUserSave(any()) } returns null
        coEvery { repository.saveUser(any()) } just Runs
        coEvery { repository.saveProgram(any()) } just Runs
        coEvery { repository.updateStatusUser(any(),any()) } just Runs

        viewModel.checkSaveOrSave()
        coVerify{
            repository.userLogIn()
            repository.getSpotifyUser()
        }
        coVerify(exactly = 0){
            repository.updateStatusUser(any(),any())
        }
    }
    @Test
    fun checkSaveOrSave_Exception2()= runTest {
        val repository = mockk<Repository>(relaxed = true)
        val viewModel = MainViewModel(repository)
        val user = UserEntity("nome", "email",true)

        coEvery { repository.userLogIn() } returns null
        coEvery { repository.getSpotifyUser() } returns user
        coEvery { repository.checkUserSave(any()) } returns null
        coEvery { repository.saveUser(any()) } just Runs
        coEvery { repository.saveProgram(any()) } just Runs
        coEvery { repository.updateStatusUser(any(),any()) } throws Exception("falha no update")

        viewModel.checkSaveOrSave()

        coVerify{
            repository.deleteUser(any())
            repository.deleteProgram(any())
        }
    }

}