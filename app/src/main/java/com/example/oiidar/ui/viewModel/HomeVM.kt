package com.example.oiidar.ui.viewModel

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
import kotlinx.coroutines.delay
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

    private lateinit var tracks: List<TrackEntity>
    private lateinit var program: ProgramaEntity
    var user: UserEntity? = null
    init {
        loadState()
        loadInit()
    }
    fun load(){
        viewModelScope.launch {
            try {
                user?.let {
                    program = repository.getProgram(it.nameId)
                    tracks = repository.getTracksUser(it.nameId)
                    _uiState.update { state->
                        state.copy(programa = program)
                    }
                    _uiState.update { state->
                        state.copy(musicas = tracks)
                    }
                    checkAndUpdateProgamStatus(it)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    fun loadInit(){
        viewModelScope.launch {
            user = repository.userLogIn()
            load()
            _uiState.update { it.copy(user = user) }
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
                    _uiState.update {
                        it.copy(carregado = true)
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

    private fun clockNowHoras(): Long{
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return Horas(now.hour.toLong(), now.minute.toLong(), now.second.toLong()).toMs()
    }
    suspend fun checkAndUpdateProgamStatus(user: UserEntity){
        val program = repository.getProgram(user.nameId)
        val now = clockNowHoras()
        if(now in program.startTime..program.finishTime){
            _uiState.update { state->
                state.copy(status = true)
            }
        }else {
            if (now < program.startTime ){
                delay(program.startTime - now)
                checkAndUpdateProgamStatus(user)
            }
            _uiState.update { state->
                state.copy(status = false)
            }
        }

    }
    fun trackNow(): Long{ // retorna o tempo que falta para a proxima musica
        val track = uiState.value.musica
        val listTracks = uiState.value.musicas
        return if(track == null) {
            // discoverTrackPlaying
            discoverTrackPlaying(listTracks)
        }else {
            // nextTrack
            nextTracks(track,listTracks)
        }
    }
    private fun discoverTrackPlaying(listTracks: List<TrackEntity>): Long{
        var delay: Long = 0
        uiState.value.programa?.let {
            var start = it.startTime
            for (t in listTracks){
                start += t.duration
                val now = clockNowHoras()
                if(start >= now){
                    _uiState.update { state->
                        state.copy(musica = t)
                    }
                    delay = start - now
                    break
                }
            }
        }
        return delay
    }
    private fun nextTracks(track: TrackEntity,listTrack: List<TrackEntity>): Long{
        val next = listTrack[listTrack.indexOf(track) + 1]
        _uiState.update { it.copy(musica = next) }
        return next.duration
    }
    fun nowTrack() :Pair<TrackEntity?, Long>{
        var musica: TrackEntity? = null
        var x: Long = 0
        uiState.value.programa?.let { program ->
            var start = program.startTime
            val horasNow = clockNowHoras()
            for(t in tracks){
                start += t.duration
                if(start >= horasNow){
                    musica = t
                    x = start - horasNow
                    break
                }
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

