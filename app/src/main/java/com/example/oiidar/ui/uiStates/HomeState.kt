package com.example.oiidar.ui.uiStates


import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity

data class HomeState (
    val user: UserEntity? = null,
    val program: ProgramaEntity? = null,
    val ms: Long = 0,
    val tracks: List<TrackEntity> = emptyList(),
    val track: TrackEntity? = null,
    val loading: String = "LOADING",
    val showEnd: Boolean = false,
    val trigger: Boolean = false,
    val status: Boolean = false,
    val onTrigger: (Boolean) -> Unit = {},
    val onShowEnd: (Boolean) -> Unit = {},
)








