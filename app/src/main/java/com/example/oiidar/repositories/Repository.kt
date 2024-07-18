package com.example.oiidar.repositories

import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.TrackItens
import com.example.oiidar.convertType.toTrackEntity
import com.example.oiidar.convertType.toUser
import com.example.oiidar.model.SpotifyPlaylist
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService

import javax.inject.Inject


class Repository @Inject constructor(
    private val dao: Dao,
    private val api: UserService,
    private val playApi: PlaylistService
){
    // ------- User -------
    private suspend fun salvaUser(user: UserEntity){
        dao.saveUser(user)
    }
    suspend fun buscaUserLogado(): UserEntity?{
        dao.getUserLogIn(true)?.also {
            return it
        }?: run {
            return null
        }
        return null
    }// TODO devolver um Boolean
    suspend fun verificarSeJaEstaSalvo(){
        val resposta = api.getUser()
        dao.getUser(resposta.id)?.let {
            dao.updateStatus(true, resposta.id)
        }?: run {
            val user = resposta.toUser()
            salvaUser(user)
            saveProgram(user)
        }
    } // TODO devolver um Boolean
    suspend fun deslogaUser(user: UserEntity){
        dao.updateStatus(false, user.nameId)
    }
    //-------- Programa -------
    suspend fun getProgram(userId: String): ProgramaEntity{
        return dao.getProgram(userId)
    }
    private suspend fun saveProgram(entity: UserEntity){
        dao.saveProgram(ProgramaEntity(entity.nameId))
    }
    suspend fun updateProgram(duration: Long, userId: String){
        val program = dao.getProgram(userId)
        val durationProgram = program.startTime + duration
        dao.updateFinishDuration(durationProgram, userId)
    }
    suspend fun updateStartProgram(start: Long, userId: String){
        dao.updateStartDuration(start, userId)
    }
    // ------- Playlist -------
    suspend fun getPlaylists(userId: String): List<PlaylistEntity>{
        return dao.getPlaylists(userId)
    }
    suspend fun responsePlaylist(idPlaylist: String): SpotifyPlaylist{
        return playApi.getPlaylist(idPlaylist)
    }
    suspend fun savePlaylist(entity: PlaylistEntity){
        dao.savePlaylist(entity)
    }
    suspend fun deletePlaylist(id: String) {
        dao.let {
            val playlist = it.getPlaylist(id)
            it.deletePlaylist(playlist)
        }
    }
    // ------- Tracks -------
    suspend fun saveTracks(lista: List<TrackItens>, playlistId: String){
        for (track in lista){
            val entity = track.track.toTrackEntity(playlistId)
            dao.saveTrack(entity)
        }
    }
    suspend fun deleteTracks(list: List<TrackEntity>){
        for (track in list){
            dao.deleteTrack(track)
        }
    }
    suspend fun getTracksPlaylist(idPlaylist: String): List<TrackEntity>{
        return dao.getTracksPlaylist(idPlaylist)
    }
    suspend fun getAllTracks(): List<TrackEntity>{
        return dao.getAllTracks()
    }// TODO atrelar musica ao user, e buscar msuica do user
}