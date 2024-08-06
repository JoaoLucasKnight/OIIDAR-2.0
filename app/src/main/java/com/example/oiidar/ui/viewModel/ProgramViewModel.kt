package com.example.oiidar.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    init { loadState() }
    fun loading(user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            try {
                loadUser(user)
                loadProgram(user)
                loadListTracks(user)
                passState("LOAD")
            }catch (e: Exception){
                passState("ERROR")
            }
        }
    }
    private suspend fun loadUser(user: UserEntity? = uiState.value.user) {
        try {
            user?. let {
                _uiState.update { state -> state.copy(user = it) }
            }?: run {
                _uiState.update { state -> state.copy(user = repository.userLogIn()!!) }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private suspend fun loadProgram(user: UserEntity? = uiState.value.user,
                                    programState: ProgramaEntity? = uiState.value.program){
        user?.let {
            val program = repository.getProgram(it.nameId)
            if(programState != program){
                _uiState.update { state ->
                    state.copy(program = program)
                }
            }
        }
    }
    private suspend fun loadListTracks(user: UserEntity? = uiState.value.user, list: List<PlaylistEntity> = uiState.value.listPlaylist){
        user?.let {
            val listEntity = repository.getPlaylists(it.nameId)
            if(list != listEntity){
                _uiState.update { state ->
                    state.copy(listPlaylist = listEntity)
                }
            }
        }
    }
    fun searchAndSave(idPlaylist: String, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let {user ->
                try {
                    repository.searchAndSave(idPlaylist,user.nameId)
                    repository.updateProgram(user.nameId)
                    loading(user)
                } catch (e: Exception){
                    passState("ERROR")
                }
            }
        }
    }
    private fun passState(pass: String){ _uiState.update { state-> state.copy(loading = pass) } }
    fun removePlaylist(idPlaylist: String, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let { user ->
                try {
                    repository.removePlaylistAndTrack(idPlaylist)
                    repository.updateProgram(user.nameId)
                    loading(user)
                } catch (e: Exception) {
                    passState("ERROR")
                }
            }?: run {passState("ERROR")}
        }
    }
    fun updateStartProgram(ms: Long, user: UserEntity? = uiState.value.user){
        viewModelScope.launch {
            user?.let { user ->
                try { repository.updateStartProgram(ms, user.nameId)
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
                onShowEnd = { show -> _uiState.update { it.copy(showEnd = !show) } },
                onShowTimer = {show -> _uiState.update { it.copy(showTimer = !show) } }
            )
        }
    }
}