package com.example.oiidar.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.convertType.toPlaylist
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.SpotifyPlaylist
import com.example.oiidar.repositories.Repository
import com.example.oiidar.ui.uiStates.ProgramaUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "OIIDAR"// TODO TERMINAR DE PADRONIZAR TAG
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
                val programa  = repository.getProgram(user.nameId)
                _uiState.update {
                    it.copy(programa = programa)
                }
                val playlists: List<PlaylistEntity> = repository.getPlaylists(user.nameId)
                _uiState.update {
                    it.copy(playlitsts = playlists)
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    suspend fun searchAndSave(idPlaylist: String){
        viewModelScope.launch {// TODO alterar para um flow
            try {
                val playlist = repository.responsePlaylist(idPlaylist)
                saveTracks(playlist)
                savePlaylist(playlist)
                updateProgramDuration()
            }
            catch (e: Exception){
                Log.d(TAG,"FALHA NA API ${e.message.toString()}")
                e.printStackTrace()
            }
        }
    }
    private suspend fun saveTracks(playlist: SpotifyPlaylist){
        viewModelScope.launch {
            try {
               repository.saveTracks(playlist.tracks.items, playlist.id)
            }catch (e: Exception){
                Log.d("error ao salvar Tracks", e.message.toString())
                e.printStackTrace()
            }
        }
    }
    private suspend fun savePlaylist(playlist: SpotifyPlaylist){
        viewModelScope.launch {
            try {
                val list = repository.getTracksPlaylist(playlist.id)
                val duration = getDurationPlaylist(list)
                val playlistEntity = playlist.toPlaylist(user.nameId, duration)
                repository.savePlaylist(playlistEntity)
            }catch (e: Exception) {
                Log.d("error ao salvar Playlist", e.message.toString())
                e.printStackTrace()
            }
        }
    }
    private suspend fun updateProgramDuration(){
        viewModelScope.launch {
            try {
                val list = repository.getPlaylists(user.nameId)
                val duration: Long = getDurationProgram(list)
                repository.updateProgram(duration, user.nameId)
            }
            catch (e: Exception){
                Log.d("falha no atualização da duração", e.message.toString())
                e.printStackTrace()
            }
        }
    }
    private fun getDurationPlaylist(listTrack: List<TrackEntity>): Long{
        var soma: Long = 0
        for (track in listTrack) soma += track.duration
        return soma
    }
    private fun getDurationProgram(list: List<PlaylistEntity>): Long{
        var soma: Long = 0
        for (playlist in list) soma += playlist.duration
        return soma
    }
    suspend fun updateProgram(){
        viewModelScope.launch {
            try {
                _uiState.update {
                   it.copy(programa = repository.getProgram(user.nameId))
                }
                _uiState.update {
                    it.copy(playlitsts = repository.getPlaylists(user.nameId))
                }
            }
            catch (e: Exception){
                Log.d("falha no atualização da duração", e.message.toString())
            }

        }
    }
    suspend fun removePlaylist(id: String){
        viewModelScope.launch {
            try {
                val list = repository.getTracksPlaylist(user.nameId)
                deleteTracks(list)
                deletePlaylist(id)
                updateProgramDuration()
            }
            catch (e: Exception){
                Log.d("falha ao apagar playlist", e.message.toString())
            }
        }
    }
    private suspend fun deleteTracks(list: List<TrackEntity>){
        viewModelScope.launch {
            try {
                repository.deleteTracks(list)
            } catch (e: Exception) {
                Log.d("falha ao apagar tracks", e.message.toString())
            }
        }
    }
    private suspend fun deletePlaylist(id: String){
        viewModelScope.launch {
            try {
                repository.deletePlaylist(id)
            } catch (e: Exception) {
                Log.d("falha ao apagar playlist", e.message.toString())
            }
        }
    }
    suspend fun updateStartProgram(ms: Long){
        viewModelScope.launch {
            try {
                repository.updateStartProgram(ms, user.nameId)
                updateProgramDuration()
            }
            catch (e: Exception){
                Log.d("falha na atualização do inicio da programação", e.message.toString())
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