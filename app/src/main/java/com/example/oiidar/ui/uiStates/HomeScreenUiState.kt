package com.example.oiidar.ui.uiStates


import android.util.Log
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas
import com.example.oiidar.model.ToMs
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.properties.Delegates

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

){
    fun msToHoras(ms: Long?): Horas{
        ms?. let {
            val segundos = (ms  / 1000) % 60
            val minutos = (ms / (1000 * 60)) % 60
            val horas = (ms / (1000 * 60 * 60))
            return Horas(horas, minutos, segundos)
        }?: return Horas(0,0,0)
    }
}








