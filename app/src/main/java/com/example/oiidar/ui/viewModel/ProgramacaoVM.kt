package com.example.oiidar.ui.viewModel

import android.util.Log
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
        load()
    }
    private fun load() {
        loadState()
        viewModelScope.launch {
            try {
                user = repository.buscaUserLogado()!!
                _uiState.update {
                    it.copy(user = user)
                }
                val programa  = repository.getPrograma(user.nameId)
                _uiState.update {
                    it.copy(programa = programa)
                }
                val playlists: List<PlaylistEntity> = repository.getAllPlaylist(user.nameId)
                _uiState.update {
                    it.copy(playlitsts = playlists)
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    suspend fun search(idPlaylist: String){
        viewModelScope.launch {
            try {
               val newPlaylist = repository.searchPlaylist(idPlaylist, user.nameId)
                newPlaylist?.let {
                    _uiState.update {
                        it.copy(playlitsts = _uiState.value.playlitsts + newPlaylist)
                    }
                    val programa = repository.getPrograma(user.nameId)
                    _uiState.update {
                        it.copy(programa = programa)
                    }
                }
            }catch (e: Exception){
                Log.d("Search", e.message.toString())
                e.printStackTrace()
            }
        }
    }
    suspend fun apagarPlaylist(id: String){
        viewModelScope.launch {
            try {
                repository.apagarPlaylist(id, user.nameId)
                _uiState.update {
                    it.copy(playlitsts = _uiState.value.playlitsts.filter {
                        it.id != id
                    })
                }
                val programa = repository.getPrograma(user.nameId)
                _uiState.update {
                    it.copy(programa = programa)
                }
            }catch (e: Exception){
                Log.d("Apagar", e.message.toString())
                e.printStackTrace()
            }
        }
    }
    suspend fun updateProgamaInicio(ms: Long){
        viewModelScope.launch {
            try {
                val programa = repository.atualizatempoInicio(ms, user.nameId)
                _uiState.update {
                    it.copy( programa = programa)
                }
            }catch (e: Exception){
                Log.d("Update", e.message.toString())
                e.printStackTrace()
            }
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