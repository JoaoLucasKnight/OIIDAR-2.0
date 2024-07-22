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

const val TAG = "OIIDAR"
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
        load()
    }
    fun load() {
        viewModelScope.launch {
            try {
                // TODO passar um bundle na navegação com o id do usuário
                user = repository.userLogIn()!!
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
                Log.d(TAG," falha no init ${e.message.toString()}")
                e.printStackTrace()
            }
        }
    }
    fun searchAndSave(idPlaylist: String){
        viewModelScope.launch {
            Log.d(TAG,"entrou searchAndSave")
            try {
                val playlist = repository.responsePlaylist(idPlaylist)
                saveTracks(playlist)
                savePlaylist(playlist)
                updateProgramDuration()
            }
            catch (e: Exception){
                Log.d(TAG,"Falha na requisição: ${e.message.toString()}")
                e.printStackTrace()
            }
            Log.d(TAG,"saiu searchAndSave")
        }
    }
    private suspend fun saveTracks(playlist: SpotifyPlaylist){
        Log.d(TAG,"entrou as tracks")
        try {
           repository.saveTracks(playlist.tracks.items, playlist.id)
        }catch (e: Exception){
            Log.d(TAG,"Falha ao salvar as tracks: ${e.message.toString()}")
            e.printStackTrace()
        }
        Log.d(TAG,"saiu as tracks")
    }
    private suspend fun savePlaylist(playlist: SpotifyPlaylist){
        Log.d(TAG,"entrou savePlaylists")
        try {
            val list = repository.getTracksPlaylist(playlist.id)
            val duration = getDurationPlaylist(list)
            val playlistEntity = playlist.toPlaylist(user.nameId, duration)
            repository.savePlaylist(playlistEntity)
        }catch (e: Exception) {
            Log.d(TAG,"Falha a o salvar a playlist: ${e.message.toString()}")
            e.printStackTrace()
        }
        Log.d(TAG,"saiu savePlaylists")
    }
    private suspend fun updateProgramDuration(){
        Log.d(TAG,"entrou updateProgramDuration")
        try {
            val list = repository.getPlaylists(user.nameId)
            val duration: Long = getDurationProgram(list)
            repository.updateProgram(duration, user.nameId)
        }
        catch (e: Exception){
            Log.d(TAG,"Falaha ao atualizar a duração: ${e.message.toString()}")
            e.printStackTrace()
        }
        Log.d(TAG,"saiu updateProgramDuration")
    }
    private fun getDurationPlaylist(listTrack: List<TrackEntity>): Long{
        Log.d(TAG,"entrou getDurationPlaylist")
        var soma: Long = 0
        for (track in listTrack) soma += track.duration
        Log.d(TAG,"saiu getDurationPlaylist $soma")
        return soma
    }
    private fun getDurationProgram(list: List<PlaylistEntity>): Long{
        Log.d(TAG,"entrou getDurationProgram")
        var soma: Long = 0
        for (playlist in list) soma += playlist.duration
        Log.d(TAG,"saiu getDurationProgram $soma")
        return soma

    }
    private suspend fun updateProgram(){
        Log.d(TAG,"entrou updateProgram")
        try {
            _uiState.update {
               it.copy(programa = repository.getProgram(user.nameId))
            }
            _uiState.update {
                it.copy(playlitsts = repository.getPlaylists(user.nameId))
            }
        }
        catch (e: Exception){
            Log.d(TAG,"Falha na atualização da UiState: ${e.message.toString()}")
        }
        Log.d(TAG,"saiu updateProgram")
    }
    fun removePlaylist(id: String){
        viewModelScope.launch {
            Log.d(TAG,"entrou removePlaylist")
            try {
                val list = repository.getTracksPlaylist(id)
                deleteTracks(list)
                deletePlaylist(id)
                updateProgramDuration()
            }
            catch (e: Exception){
                Log.d(TAG,"Falha fun removePlaylist: ${e.message.toString()}")
            }
            Log.d(TAG,"saiu removePlaylist")
        }

    }
    private suspend fun deleteTracks(list: List<TrackEntity>){
        Log.d(TAG,"entrou deleteTracks")
        try {
            repository.deleteTracks(list)
        } catch (e: Exception) {
            Log.d(TAG,"Falha ao apagar tracks ${e.message.toString()}")
        }
        Log.d(TAG,"saiu deleteTracks")
    }
    private suspend fun deletePlaylist(id: String){
        Log.d(TAG,"entrou deletePlaylist")
        try {
            repository.deletePlaylist(id)
        } catch (e: Exception) {
            Log.d(TAG,"Falha ao apagar playlist ${e.message.toString()}")
        }
        Log.d(TAG,"saiu deletePlaylist")
    }
    fun updateStartProgram(ms: Long){
        viewModelScope.launch {
            Log.d(TAG,"entrou updateStartProgram")
            try {
                repository.updateStartProgram(ms, user.nameId)
                updateProgramDuration()
            }
            catch (e: Exception){
                Log.d(TAG,"Falha na atualização do inicio da programação: ${e.message.toString()}")
            }
            Log.d(TAG,"saiu updateStartProgram")
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
                },
                onUpdateProgram = {
                    viewModelScope.launch {
                        updateStartProgram(it)
                        updateProgram()
                    }
                },
                onAddPlaylist = {
                    viewModelScope.launch {
                        searchAndSave(it)
                        updateProgram()
                    }
                },
                onRemovePlaylist = {
                    viewModelScope.launch {
                        removePlaylist(it)
                        updateProgram()
                    }
                }
            )
        }
    }
}