package com.example.oiidar.ui.viewModel


import com.example.oiidar.model.Images
import com.example.oiidar.model.SpotifyPlaylist
import com.example.oiidar.model.Tracks
import com.example.oiidar.repositories.Repository
import com.example.oiidar.ui.uiStates.ProgramaUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

import org.junit.Test


class ProgramacaoVMTest{
    @Test
    fun checkIfSearchAndSave()=runTest {
        //Create an active task     //Give
        val repository = mockk<Repository>()
        val viewModel = ProgramacaoVM(repository)
        val img = Images("imgTest")
        val spotifyPlaylist = SpotifyPlaylist(
            "hrefTest","idTest",listOf(img),"nameTest", mockk<Tracks>(), "uriTest"
        )
        coEvery {
            repository.responsePlaylist("idplaylist")
        } returns(spotifyPlaylist)
        coEvery {
            viewModel.load()
        }returns(Unit)


        //Call your function    // When
        viewModel.searchAndSave("idplaylist")


        //Check the result      // Then
        coVerify {
            repository.responsePlaylist("idplaylist")
        }

    }

}