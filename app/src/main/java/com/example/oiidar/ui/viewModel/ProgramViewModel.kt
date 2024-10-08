package com.example.oiidar.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    init { loadState() }
    fun loading(user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            try {
                user?.let {
                    loadProgram(it)
                    loadListTracks(it)
                    passState("LOAD")
                }?: run {
                    loadUser()
                }
            } catch (e: Exception){
                passState("ERROR")
                e.printStackTrace()
            }
        }
    }
    private fun loadUser(){
        viewModelScope.launch {
            try {
                _uiState.update { state ->
                    state.copy(user = repository.userLogIn()!!)
                }
                loadProgram()
                loadListTracks()
                passState("LOAD")
            } catch (e: Exception) {
                passState("ERROR")
                e.printStackTrace()
            }
        }
    }
    private suspend fun loadProgram(user: UserEntity? = uiState.value.user){
        user?.let { _uiState.update { state -> state.copy(program = repository.getProgram(user)) } }
    }
    private suspend fun loadListTracks(user: UserEntity? = uiState.value.user){
        user?.let { _uiState.update { state -> state.copy(listPlaylist = repository.getListPlaylists(user)) } }
    }
    fun searchAndSave(idPlaylist: String, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let {user ->
                try {
                    repository.searchAndSave(idPlaylist,user)
                    repository.updateProgram(user)
                    loading(user)
                } catch (e: Exception){
                    e.printStackTrace()
                    passState("ERROR")
                }
            }?: run {passState("ERROR")}
        }
    }
    private fun passState(pass: String){ _uiState.update { state-> state.copy(loading = pass) } }
    fun removePlaylist(idPlaylist: String, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let { user ->
                try {
                    repository.removePlaylistAndTrack(idPlaylist)
                    repository.updateProgram(user)
                    loading(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                    passState("ERROR")
                }
            }?: run {passState("ERROR")}
        }
    }
    fun updateStartProgram(ms: Long, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let { user ->
                try {
                    repository.updateStartProgram(ms, user)
                    loading(user)
                }
                catch (e: Exception){
                    passState("ERROR")
                    e.printStackTrace()
                }
            }?: run {passState("ERROR")}
        }
    }
    private fun loadState(){
        _uiState.update{empty ->
            empty.copy(
                onUrl = {url -> _uiState.update { it.copy(url = url) } },
                onShowEnd = { show -> _uiState.update { it.copy(showEnd = show) } },
                onShowTimer = {show -> _uiState.update { it.copy(showTimer = show) } }
            )
        }
    }
}