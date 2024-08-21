package com.example.oiidar.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.conectionApi.Spotify
import com.example.oiidar.contantes.TAG
import com.example.oiidar.convertType.toMs
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.Horas
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
    fun loading(){
        viewModelScope.launch {
            try {
                val user = uiState.value.user
                user?.let {
                    loadProgram(it)
                    loadTracks(it)
                    passState("LOAD")
                }?: run {
                    loadUser()
                }
                checkAndUpdateProgramStatus()
            }catch (e: Exception){
                passState("ERROR")
                Log.i(TAG, "loading: ${e.message}")
            }

        }
    }
    private fun passState(pass: String){ _uiState.update { state-> state.copy(loading = pass) } }
     suspend fun loadUser(){
         try {
             _uiState.update { state -> state.copy(user = repository.userLogIn()!!) }
             loadProgram()
             loadTracks()
             passState("LOAD")
         }catch (e: Exception){
             e.printStackTrace()
             passState("ERROR")
         }
    }
    private suspend fun loadProgram(user: UserEntity? = uiState.value.user){
        user?.let { _uiState.update { state-> state.copy(program = repository.getProgram(user)) } }
    }
    private suspend fun loadTracks(user: UserEntity? = uiState.value.user){
        user?.let { _uiState.update { state-> state.copy(tracks = repository.getTracksUser(user)) } }
    }
    private  fun loadState(){
        _uiState.update { stateInitial ->
            stateInitial.copy(
                onShowEnd = { show-> _uiState.update { it.copy(showEnd = show) } },
                onTrigger = { trigger -> _uiState.update { it.copy(trigger = trigger) } }
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
            Log.d(TAG, "in status Program")
            if(now in program.startTime..program.finishTime){
                _uiState.update { state-> state.copy(status = true) }
                discoverTrackPlaying()
            }else { _uiState.update { state-> state.copy(status = false) } }
        }
    }
    private fun updateTrackMs(ms: Long, track: TrackEntity?){
        _uiState.update { state-> state.copy(ms = ms) }
        _uiState.update { state-> state.copy(track = track) }
    }
    fun discoverTrackPlaying(
        program: ProgramaEntity? = uiState.value.program,
        listTracks: List<TrackEntity> = uiState.value.tracks,
        now: Long = clockNowHoras()
    ){
        program?.let {
            var start = it.startTime
            if((now > start) and (now < it.finishTime)){
                for (t in listTracks){
                    start += t.duration
                    if(start >= now ){
                        updateTrackMs(start-now, t)
                        break
                    }
                }
            }
        }

    }
    fun nextTrack(
        track: TrackEntity = uiState.value.track!!,
        listTrack: List<TrackEntity> = uiState.value.tracks
    ){
        if (track != listTrack.last()){
            val nowTrack = listTrack[listTrack.indexOf(track) + 1]
            val delay = nowTrack.duration
            updateTrackMs(delay, nowTrack)
        }
    }

    fun checkMoved(
        user: UserEntity? = uiState.value.user,
        program: ProgramaEntity? = uiState.value.program
    ){
        viewModelScope.launch {
            if ((user != null) and (program != null)){
                if(program != repository.getProgram(user!!)){
                    loading()
                }
            }
        }
    }

    // --------- Spotify SDK ---------
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

