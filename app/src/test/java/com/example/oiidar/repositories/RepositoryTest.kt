package com.example.oiidar.repositories

import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    lateinit var dao: Dao
    lateinit var api: UserService
    lateinit var playApi: PlaylistService
    lateinit var repository: Repository


    private val user = UserEntity("nome", "email",true)
@Before
fun setUp() {
    dao = mockk<Dao>()
    api = mockk<UserService>()
    playApi = mockk<PlaylistService>()
    repository = Repository(dao, api, playApi)
}

    @Test
    fun test_userLogIn()= runTest {
        coEvery { dao.getUserLogIn() } returns user

        repository.userLogIn()

        coVerify { dao.getUserLogIn() }
    }
    @Test
    fun test_checkUserSave()= runTest {
        coEvery { dao.getUser(user.nameId) } returns user

        val result = repository.checkUserSave(user)

        coVerify { dao.getUser(user.nameId) }
        assertEquals(result, user)
    }
    @Test
    fun test_checkUserSave_Null()= runTest {
        coEvery { dao.getUser(user.nameId) } returns null

        val result = repository.checkUserSave(user)

        coVerify { dao.getUser(user.nameId) }
        assertEquals(result, null)
    }
    @Test
    fun test_updateStatusUser()= runTest {
        coEvery { dao.updateStatus(any(),user.nameId) } just Runs

        repository.updateStatusUser(false,user)

        coVerify { dao.updateStatus(false,user.nameId) }
    }

    @Test
    fun test_getProgram()= runTest {
        coEvery { dao.getProgram(any()) } returns mockk<ProgramaEntity>()

        repository.getProgram(user.nameId)

        coVerify { dao.getProgram(user.nameId) }
    }

    @Test
    fun test_updateStartProgram()= runTest {
        coEvery { dao.updateStartDuration(any(),user.nameId) } just Runs
    }
    @Test
    fun test_saveUserAndProgram()= runTest {
        coEvery { dao.saveUser(user) } just Runs
        coEvery { dao.saveProgram(ProgramaEntity(user.nameId)) } just Runs
        coEvery { dao.updateStatus(true,user.nameId) } just Runs

        repository.saveUserAndProgram(user)

        coVerifySequence{
            dao.saveUser(user)
            dao.saveProgram(ProgramaEntity(user.nameId))
            dao.updateStatus(true,user.nameId)
        }
    }

}