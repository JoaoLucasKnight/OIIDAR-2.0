package com.example.oiidar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.oiidar.model.SpotifyPlaylist
@Entity
data class ProgramaEntity(
    @PrimaryKey
    val id: String ,
    val tempoInicio: Long,
    val tempoFinal: Long,
)
