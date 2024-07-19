package com.example.oiidar.ui.uiStates

import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas

data class ProgramaUiState(
    val user: UserEntity? = null,
    val programa: ProgramaEntity? = null,
    val playlitsts: List<PlaylistEntity> = emptyList(),
    val playlist: PlaylistEntity? = null,
    val horas: Horas = Horas(0,0,0),
    val showSair: Boolean = false,
    val showTimer: Boolean = false,
    val url: String = "",

    val onUrl: (String) -> Unit = {},
    val onShowSair: (Boolean) -> Unit = {},
    val onShowTimer: (Boolean) -> Unit = {},
    val onAddPlaylist: (idPlaylist: String) -> Unit = {},
    val onRemovePlaylist: (idPlaylist: String) -> Unit = {},
    val onUpdateProgram: (ms: Long) -> Unit = {},
){
    fun conversorMs(ms: Long?): String {
        ms?. let {
            val segundos = (ms  / 1000) % 60
            val minutos = (ms / (1000 * 60)) % 60
            val horas = (ms / (1000 * 60 * 60))
            return String.format("%02d:%02d:%02d", horas, minutos, segundos)
        }?: return "00:00:00"
    }

    fun msToHoras(ms: Long?): Horas {
        ms?. let {
            val segundos = (ms  / 1000) % 60
            val minutos = (ms / (1000 * 60)) % 60
            val horas = (ms / (1000 * 60 * 60))
            return Horas(horas, minutos, segundos)
        }?: return Horas(0,0,0)
    }
    fun horasToMs(Horas: Horas): Long{
        return Horas.horas * 3600000 + Horas.minutos * 60000 + Horas.segundos * 1000
    }
}

