package com.example.oiidar.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.conectionApi.Spotify
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas
import com.example.oiidar.convertType.toMs
import com.example.oiidar.repositories.Repository
import com.example.oiidar.ui.uiStates.HomeScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val repository: Repository,
):ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    lateinit var tracks: List<TrackEntity>
    lateinit var program: ProgramaEntity
    lateinit var user: UserEntity
    init {
        loadState()
        viewModelScope.launch {
            try {
                user = repository.userLogIn()!!
                _uiState.update { it.copy(user = user) }
            } catch (e: Exception) {
                Log.e("OIIDAR", e.message.toString())
                e.printStackTrace() }
            load()
        }
    }
    private suspend fun load(){
        program = repository.getProgram(user.nameId)
        tracks = repository.getTracksUser(user.nameId)
        _uiState.update {
            it.copy(programa = program)
        }
        _uiState.update {
            it.copy(musicas = tracks)
        }
    }
    private  fun loadState(){
        _uiState.update { estadoVazio ->
            estadoVazio.copy(
                onShowSair = { show->
                    _uiState.update {
                        it.copy(showSair = !show)
                    }
                },
                onStatus = { status ->
                     _uiState.update {
                         it.copy(status = status)
                     }
                },
                onMusica = {
                    val (now,delay) = nowTrack()
                    _uiState.update {
                        it.copy(del = delay)
                    }
                    _uiState.update {
                        it.copy(musica = now)
                    }
                },
                proxima = {track ->
                    val (next,delay) = nextTrack(track)
                    _uiState.update {
                        it.copy(del = delay)
                    }
                    _uiState.update {
                        it.copy(musica = next)
                    }
                },
                 atualiza = {
                    viewModelScope.launch {
                        load()
                        _uiState.update {
                            it.copy(carregado = true)
                        }
                    }
                },
                onGatilho = {gatilho ->
                    _uiState.update {
                        it.copy(gatilho = gatilho)
                    }
                }
            )
        }
    }
    fun nowTrack() :Pair<TrackEntity?, Long>{
        var program = program.startTime
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val horasNow = Horas(now.hour.toLong(), now.minute.toLong(), now.second.toLong()).toMs()
        lateinit var musica: TrackEntity
        var x: Long = 0
        for(t in tracks){
            program += t.duration
            if(program >= horasNow){
                musica = t
                x = program - horasNow
                break
            }
        }
        return Pair(musica, x)
    }
    fun nextTrack(track: TrackEntity): Pair<TrackEntity, Long>{
        val nextTrack = tracks[tracks.indexOf(track) + 1]
        return Pair(nextTrack, nextTrack.duration)
    }
    fun playTrack(track: TrackEntity?){
        track?.let {
            Spotify.tocar(track.uri)
            addQueue(track)
        }
    }
    private fun addQueue(track: TrackEntity){
        val x : Int = tracks.indexOf(track) + 1
        for(i in x until tracks.size){
            val uri = tracks[i].uri
            Spotify.adionarFila(uri)
        }
    }
}

