package com.example.oiidar.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class AuthVMTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun checkSaveOrSave()= runTest {
        val user = UserEntity("nome", "email",false)
        val userNull: UserEntity? = null
        val repository = mockk<Repository>(relaxed = true)
        val viewModel = AuthVM(repository)

        coEvery { repository.userLogIn() } returns user
        coEvery { repository.getSpotifyUser() } returns user
        coEvery { repository.checkUserSave(user.nameId) } returns userNull
        coEvery { repository.saveUser(user) } just Runs
        coEvery { repository.saveProgram(user) } just Runs
        coEvery { repository.updateStatusUser(true, user.nameId) } just Runs

        viewModel.checkSaveOrSave()

        coVerify {
            repository.getSpotifyUser()
            repository.userLogIn()
            repository.checkUserSave(user.nameId)
            repository.saveUser(user)
            repository.saveProgram(user)
            repository.updateStatusUser(true, user.nameId)
        }
    }
}