package com.example.oiidar.ui.viewModel

import androidx.compose.material3.SnackbarDuration
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.repositories.Repository
import io.mockk.every
import io.mockk.mockk
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ProgramacaoVMTest{
    @Test
    fun `testar a funcao ta somando certo`(){
        //Create an active task
        //Give
        val vm = mockk<ProgramacaoVM>()
        val list = listOf<TrackEntity>(
            TrackEntity(
                playlistId = "play1",
                id = "id1",
                name = "name1",
                img = "img1",
                uri = "uri1",
                duration = 1
            ),
            TrackEntity(
                playlistId = "play1",
                id = "id1",
                name = "name1",
                img = "img1",
                uri = "uri1",
                duration = 1
            ),
            TrackEntity(
                playlistId = "play1",
                id = "id1",
                name = "name1",
                img = "img1",
                uri = "uri1",
                duration = 1
            )
        )

        //Call your function
        // When

        //Check the result
        // Then
    }

}