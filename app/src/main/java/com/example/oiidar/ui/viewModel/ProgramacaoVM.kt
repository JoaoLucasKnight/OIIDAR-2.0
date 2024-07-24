package com.example.oiidar.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import com.example.oiidar.ui.uiStates.ProgramaUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramacaoVM @Inject constructor(
    private val repository: Repository
): ViewModel() {
    private val _uiState = MutableStateFlow(ProgramaUiState())
    val uiState = _uiState.asStateFlow()
    lateinit var user: UserEntity
        private set
    init {
        loadState()
        viewModelScope.launch {
            try {
                user = repository.userLogIn()!!
                _uiState.update { it.copy(user = user) }
            }catch (e: Exception) { e.printStackTrace() }
            load()
        }
    }
    private suspend fun load() {
        try {
            val program  = repository.getProgram(user.nameId)
            _uiState.update { it.copy(programa = program) }
            val playlists: List<PlaylistEntity> = repository.getPlaylists(user.nameId)
            _uiState.update { it.copy(playlitsts = playlists) }
        }catch (e: Exception) { e.printStackTrace() }
    }
    fun searchAndSave(idPlaylist: String){
        viewModelScope.launch {
            try {
                repository.let {
                    it.searchAndSave(idPlaylist,user.nameId)
                    it.updateProgram(user.nameId)
                }
            } catch (e: Exception){ e.printStackTrace() }
        }
    }
    fun removePlaylist(idPlaylist: String){
        viewModelScope.launch {
            try {
                repository.let {
                    it.removePlaylistAndTrack(idPlaylist)
                    it.updateProgram(user.nameId)
                }
            }
            catch (e: Exception){ e.printStackTrace() }
        }
    }
    fun updateStartProgram(ms: Long){
        viewModelScope.launch {
            try {
                repository.let {
                    it.updateStartProgram(ms, user.nameId)
                    it.updateProgram(user.nameId)
                }
            }
            catch (e: Exception){ e.printStackTrace() }
        }
    }
    private fun loadState(){
        _uiState.update{vazio ->
            vazio.copy(
                onUrl = {url ->
                    _uiState.update {
                        it.copy(url = url)
                    }
                },
                onShowSair = {show ->
                    _uiState.update {
                        it.copy(showSair = !show)
                    }
                },
                onShowTimer = {show ->
                    _uiState.update {
                        it.copy(showTimer = !show)
                    }
                }
            )
        }
    }
}