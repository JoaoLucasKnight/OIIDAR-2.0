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
    private val repository = mockk<Repository>()
    private val vm = ProgramacaoVM(repository)
    @Test
    fun testSearchAndSave()=runTest {
        val idPlaylist= "id test"
        val playlist = mockk<SpotifyPlaylist>()
        coEvery { repository.responsePlaylist(idPlaylist) } returns playlist
        vm.searchAndSave("id test")
    }
}