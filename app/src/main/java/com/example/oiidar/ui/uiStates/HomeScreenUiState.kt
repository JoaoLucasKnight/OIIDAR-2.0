package com.example.oiidar.ui.uiStates


import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas

data class HomeScreenUiState (
    val user: UserEntity? = null,
    val programa: ProgramaEntity? = null,
    val musicas: List<TrackEntity> = emptyList(),
    val musica: TrackEntity? = null,

    val showSair: Boolean = false,
    val gatilho: Boolean = false,
    val status: Boolean = false,
    val horas: Horas = Horas(0,0,0),
    val del: Long = 0,

    val carregado: Boolean = false,
    val atualiza: () -> Unit = {},
    val onGatilho: (Boolean) -> Unit = {},
    val onShowSair: (Boolean) -> Unit = {},
    val onStatus: (Boolean) -> Unit = {},
    val onMusica: () -> Unit = {},
    val proxima: (TrackEntity) -> Unit = {},


    val onPlay: (String) -> Unit = {},

)








