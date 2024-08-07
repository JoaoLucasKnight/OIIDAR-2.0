package com.example.oiidar.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.conectionApi.Spotify
import com.example.oiidar.contantes.TAG
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas
import com.example.oiidar.convertType.toMs
import com.example.oiidar.repositories.Repository
import com.example.oiidar.ui.uiStates.HomeState
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
class HomeViewModel @Inject constructor(
    private val repository: Repository,
):ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()
    init { loadState() }

    //Todo separar logica dentro do user
    fun loading(){
        viewModelScope.launch {
            try {
                val user = uiState.value.user
                user?.let {
                    loadProgram(it)
                }?: run {
                    loadUser()
                }
            }catch (e: Exception){
                _uiState.update { state-> state.copy(loading = "ERROR") }
                Log.i(TAG, "loading: ${e.message}")
            }
        }
    }
     suspend fun loadUser(){
        val user = repository.userLogIn()
        user?. let{
            _uiState.update { state-> state.copy(user = it) }
            loadProgram(it)
        }?: run {
            _uiState.update { state-> state.copy(loading = "ERROR") }
            Log.i(TAG, "loading: user == null")
        }
    }
    private suspend fun loadProgram(user: UserEntity, programState: ProgramaEntity? = uiState.value.program){
        val program = repository.getProgram(user.nameId)
        if(programState != program){
            _uiState.update { state ->
                state.copy(program = program)
            }
            loadTracks(user)
        }
        _uiState.update { state-> state.copy(loading = "LOAD") }
    }
    private suspend fun loadTracks(user: UserEntity){
        val tracks = repository.getTracksUser(user.nameId)
        _uiState.update { state-> state.copy(tracks = tracks) }
    }

    private  fun loadState(){
        _uiState.update { stateInitial ->
            stateInitial.copy(
                onShowEnd = { show->
                    _uiState.update {
                        it.copy(showEnd = !show)
                    }
                },
                onTrigger = { trigger ->
                    _uiState.update {
                        it.copy(trigger = trigger)
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
        program: ProgramaEntity? = uiState.value.program,
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
    fun discoverTrackPlaying(
        program: ProgramaEntity? = uiState.value.program,
        listTracks: List<TrackEntity> = uiState.value.tracks,
        now: Long = clockNowHoras()
    ): Long{
        var delay: Long = 0
        program?.let {
            var start = it.startTime
            if(now < start) return 0
            for (t in listTracks){
                start += t.duration
                if(start >= now ){
                    _uiState.update { state->
                        state.copy(track = t)
                    }
                    delay = start - now
                    break
                }
            }
        }

        return delay
    }
    fun nextTrack(
        track: TrackEntity = uiState.value.track!!,
        listTrack: List<TrackEntity> = uiState.value.tracks
    ): Long{
        if (track == listTrack.last()){
            _uiState.update { state->
                state.copy(track = null)
            }
            return 0
        }else {
            val next = listTrack[listTrack.indexOf(track) + 1]
            _uiState.update { it.copy(track = next) }
            return next.duration
        }
    }
    fun playTrack(track: TrackEntity?){
        track?.let {
            Spotify.tocar(track.uri)
            addQueue(track)
        }
    }
    private fun addQueue(
        track: TrackEntity? = uiState.value.track,
        tracks: List<TrackEntity> = uiState.value.tracks
    ){
        val x : Int = tracks.indexOf(track) + 1
        for(i in x until tracks.size){
            val uri = tracks[i].uri
            Spotify.adionarFila(uri)
        }
    }
}

