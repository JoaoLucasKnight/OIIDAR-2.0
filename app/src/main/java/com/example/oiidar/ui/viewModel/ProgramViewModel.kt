package com.example.oiidar.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.contantes.TAG
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import com.example.oiidar.ui.uiStates.ProgramState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    private val _uiState = MutableStateFlow(ProgramState())
    val uiState = _uiState.asStateFlow()
    init {
        loadState()
        loading()
    }
    fun loading(){
        viewModelScope.launch {
            try {
                val user = uiState.value.user
                user?.let {
                    loadProgram(it)
                    loadListTracks(it)
                    passState("LOAD")
                }?: run {
                    loadUser()
                    passState("LOAD")
                }
            }catch (e: Exception){
                passState("ERROR")
            }
        }
    }
    private suspend fun loadUser(){
        val user = repository.userLogIn()
        user?. let{
            _uiState.update { state-> state.copy(user = it) }
            loadProgram(it)
            loadListTracks(it)
        }?: run {
            passState("ERROR")
        }
    }
    private suspend fun loadProgram(user: UserEntity, programState: ProgramaEntity? = uiState.value.program){
        val program = repository.getProgram(user.nameId)
        if(programState != program){
            Log.d(TAG, "Update: $program")
            _uiState.update { state ->
                state.copy(program = program)
            }
        }
    }
    private suspend fun loadListTracks(user: UserEntity, list: List<PlaylistEntity> = uiState.value.listPlaylist){
        val listEntity = repository.getPlaylists(user.nameId)
        if(list != listEntity){
            _uiState.update { state ->
                state.copy(listPlaylist = listEntity)
            }
        }
    }
    fun searchAndSave(idPlaylist: String, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let {user ->
                try {
                    repository.searchAndSave(idPlaylist,user.nameId)
                    repository.updateProgram(user.nameId)

                    loading()
                } catch (e: Exception){
                    passState("ERROR")
                    e.printStackTrace()
                }
            }?: run {loading()}
        }
    }
    private fun passState(pass: String){
        _uiState.update { state-> state.copy(loading = pass) }
    }
    fun removePlaylist(idPlaylist: String, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let { user ->
                try {
                    repository.removePlaylistAndTrack(idPlaylist)
                    repository.updateProgram(user.nameId)
                    loading()
                } catch (e: Exception) {
                    passState("ERROR")
                    e.printStackTrace()
                }
            }?: run {loading()}
        }
    }
    fun updateStartProgram(ms: Long, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let { user ->
                try { repository.updateStartProgram(ms, user.nameId)
                    loading()
                }
                catch (e: Exception){
                    passState("ERROR")
                    e.printStackTrace()
                }
            }?: run {loading()}
        }
    }
    private fun loadState(){
        _uiState.update{empty ->
            empty.copy(
                onUrl = {url -> _uiState.update { it.copy(url = url) } },
                onShowEnd = { show -> _uiState.update { it.copy(showEnd = !show) } },
                onShowTimer = {show -> _uiState.update { it.copy(showTimer = !show) } }
            )
        }
    }
}