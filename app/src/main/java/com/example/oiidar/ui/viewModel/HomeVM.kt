package com.example.oiidar.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.conectionApi.Spotify
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas
import com.example.oiidar.model.ToMs
import com.example.oiidar.repositories.UserRepository
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
    private val repository: UserRepository,
):ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    lateinit var musicas: List<TrackEntity>
    lateinit var programa: ProgramaEntity
    lateinit var user: UserEntity
    init {
        loadUser()
    }
    private fun loadUser() {
        viewModelScope.launch {
            try {
                user = repository.buscaUserLogado()!!
                _uiState.update {
                    it.copy(user = user)
                }
                loadState()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    suspend fun atualiza(){
        programa = repository.getPrograma(user.nameId)
        musicas = repository.getAllTracks()
        _uiState.update {
            it.copy(programa = programa)
        }
        _uiState.update {
            it.copy(musicas = musicas)
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
                    val (musica,delay) = atualTrack()
                    _uiState.update {
                        it.copy(del = delay)
                    }
                    _uiState.update {
                        it.copy(musica = musica)
                    }
                },
                proxima = {track ->
                    val (musica,delay) = proximaTrack(track)
                    _uiState.update {
                        it.copy(del = delay)
                    }
                    _uiState.update {
                        it.copy(musica = musica)
                    }
                },
                 atualiza = {
                    viewModelScope.launch {
                        atualiza()
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
    fun atualTrack() :Pair<TrackEntity?, Long>{
        var prog = programa.tempoInicio
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val horasNow = Horas(now.hour.toLong(), now.minute.toLong(), now.second.toLong()).ToMs()
        lateinit var musica: TrackEntity
        var x: Long = 0
        for(t in musicas){
            prog += t.duration
            if(prog >= horasNow){
                musica = t
                x = prog - horasNow
                break
            }
        }
        return Pair(musica, x)
    }
    fun proximaTrack(track: TrackEntity): Pair<TrackEntity, Long>{
        val musica = musicas[musicas.indexOf(track) + 1]
        return Pair(musica, musica.duration)
    }
    fun tocar(musica: TrackEntity?){
        musica?.let {
            Spotify.tocar(musica.uri)
            viewModelScope.launch {
                addFila(musica)
            }
        }
    }
    private fun addFila(musica: TrackEntity){
        var indice: Int = musicas.indexOf(musica) + 1
        for(i in indice until musicas.size){
            val uri = musicas[i].uri
            Spotify.adionarFila(uri)
        }
    }
}

