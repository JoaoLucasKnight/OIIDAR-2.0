package com.example.oiidar.repositories

import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService
import io.mockk.coEvery
import io.mockk.coVerify

import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class RepositoryUserTest{
    private val dao = mockk<Dao>()
    private val api = mockk<UserService>()
    private val playApi = mockk<PlaylistService>()
    private val repository = Repository(dao,api,playApi)
    @Test
    fun testSaveUser()= runTest{
        val user =mockk<UserEntity>()
        coEvery { dao.saveUser(user) } returns Unit
        repository.saveUser(user)
        coVerify{ dao.saveUser(user) }
    }
    @Test
    fun testUserLogIn()= runTest{
        val user =mockk<UserEntity>()
        coEvery { dao.getUserLogIn() } returns user
        repository.userLogIn()
        coVerify { dao.getUserLogIn() }
    }
    @Test
    fun testCheckUserSave()= runTest{
        val user =mockk<UserEntity>()
        val id = "test"
        coEvery { dao.getUser(id) } returns user
        repository.checkUserSave(id)
        coVerify { dao.getUser(id) }
    }
    @Test
    fun testUpdateStatusUser()= runTest {
        val user = UserEntity("test","test",true)
        coEvery { dao.updateStatus(!user.status, user.nameId) } returns Unit
        repository.updateStatusUser(false, user.nameId)
        coVerify {
            dao.updateStatus(!user.status, user.nameId)
        }
    }

}