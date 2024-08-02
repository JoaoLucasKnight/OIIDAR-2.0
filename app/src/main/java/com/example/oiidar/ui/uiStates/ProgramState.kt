package com.example.oiidar.ui.uiStates

import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.UserEntity

data class ProgramState(
    val user: UserEntity? = null,
    val program: ProgramaEntity? = null,
    val listPlaylist: List<PlaylistEntity> = emptyList(),
    val playlist: PlaylistEntity? = null,
    val loading: String = "LOADING",
    val showEnd: Boolean = false,
    val showTimer: Boolean = false,
    val url: String = "",
    val onUrl: (String) -> Unit = {},
    val onShowEnd: (Boolean) -> Unit = {},
    val onShowTimer: (Boolean) -> Unit = {},
)


