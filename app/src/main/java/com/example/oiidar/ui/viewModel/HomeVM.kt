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
        //  Tirei o load Init do init
    }

    fun loading(){
        viewModelScope.launch {
           val user = repository.userLogIn()
            user?. let {
                _uiState.update { state-> state.copy(user = it) }
                program = repository.getProgram(it.nameId)
                _uiState.update {state -> state.copy(programa = program) }
                if(program.startTime != program.finishTime){
                    val listTrack = repository.getTracksUser(it.nameId)
                    _uiState.update { state-> state.copy(musicas = listTrack) }
                }
                _uiState.update { state-> state.copy(loading = "load") }
            }?: run {
                Log.i("OIIDAR", "loading: user == null")
            }
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
    fun checkAndUpdateProgramStatus(
        program: ProgramaEntity? = uiState.value.programa,
        now: Long = clockNowHoras())
    {
        program?.let {
            if(now in program.startTime..program.finishTime){
                _uiState.update { state->
                    state.copy(status = true)
                }
            }else {
                _uiState.update { state->
                    state.copy(status = false)
                }
            }
        }
    }
    fun trackNow(
        program: ProgramaEntity? = uiState.value.programa,
        track: TrackEntity? = uiState.value.musica,
        listTracks: List<TrackEntity> = uiState.value.musicas,
        now :Long = clockNowHoras()
    ): Long{
        return if(track == null) {
            // discoverTrackPlaying
            discoverTrackPlaying(program, listTracks, now)
        }else {
            // nextTrack
            nextTracks(track,listTracks)
        }
    }
    private fun discoverTrackPlaying(program: ProgramaEntity?,listTracks: List<TrackEntity>, now: Long = clockNowHoras()): Long{
        var delay: Long = 0
        program?.let {
            var start = it.startTime
            if(now < start) return 0
            for (t in listTracks){
                start += t.duration
                if(start >= now ){
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
        if (track == listTrack.last()){
            _uiState.update { state->
                state.copy(musica = null)
            }
            return 0
        }else {
            val next = listTrack[listTrack.indexOf(track) + 1]
            _uiState.update { it.copy(musica = next) }
            return next.duration
        }
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

