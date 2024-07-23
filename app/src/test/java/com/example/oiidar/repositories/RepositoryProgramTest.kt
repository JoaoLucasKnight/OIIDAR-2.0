package com.example.oiidar.repositories

import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test


class RepositoryProgramTest {
    private val dao = mockk<Dao>()
    private val api = mockk<UserService>()
    private val playApi = mockk<PlaylistService>()
    private val repository = Repository(dao,api,playApi)
    @Test
    fun testGetProgram()= runTest {
        val id ="test"
        val program = mockk<ProgramaEntity>()
        coEvery { dao.getProgram(id) } returns program
        repository.getProgram(id)
        coVerify { dao.getProgram(id) }
    }
    @Test
    fun testSaveProgram()= runTest {
        val user= UserEntity("test","test", false)
        val program = ProgramaEntity(user.nameId)
        coEvery { dao.saveProgram(program) } returns Unit
        repository.saveProgram(user)
        coVerify { dao.saveProgram(program) }
    }
    @Test
    fun testUpdateProgram()= runTest {
        val duration: Long = 1000
        val program = ProgramaEntity("test")
        coEvery { dao.updateFinishDuration(duration, program.id)} returns Unit
        repository.updateProgram(duration, program)
        coVerify { dao.updateFinishDuration(duration, program.id) }
    }
    @Test
    fun testUpdateProgramStart()= runTest {
        val duration: Long = 1000
        val id = "test"
        coEvery { dao.updateStartDuration(duration, id)} returns Unit
        repository.updateStartProgram(duration, id)
        coVerify { dao.updateStartDuration(duration, id) }
    }
}