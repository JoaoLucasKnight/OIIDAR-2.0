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

class RepositoryTest{
    private val dao = mockk<Dao>()
    private val api = mockk<UserService>()
    private val playApi = mockk<PlaylistService>()


    @Test
    fun testSaveUSer()= runTest{
        val repository = Repository(dao,api,playApi)
        val user =mockk<UserEntity>()
        coEvery {
            dao.saveUser(user)
        } returns Unit

        repository.saveUser(user)

        coVerify{
            dao.saveUser(user)
        }

    }

}