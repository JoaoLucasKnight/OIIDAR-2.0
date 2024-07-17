package com.example.oiidar.repositories

import android.util.Log
import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.TrackItens
import com.example.oiidar.convertType.toPlaylist
import com.example.oiidar.convertType.toTrackEntity
import com.example.oiidar.convertType.toUser
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
        dao.salvarUser(user)
    }
    suspend fun buscaUserLogado(): UserEntity?{
        dao.getUserLogado(true)?.also {
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
            salvaProg(user)
        }
    } // TODO devolver um Boolean
    suspend fun deslogaUser(user: UserEntity){
        dao.updateStatus(false, user.nameId)
    }


    //-------- Programa -------
    suspend fun getPrograma(userId: String): ProgramaEntity{
        return dao.retornaPrograma(userId)
    }
    suspend private fun salvaProg(entity: UserEntity){
        dao.salvarPrograma(ProgramaEntity(entity.nameId))
    }
    suspend fun atualizatempoInicio(ms: Long, userId: String ): ProgramaEntity{
        val prog = dao.retornaPrograma(userId)
        val resultado = (ms - prog.startTime)+prog.finishTime

        dao.updateDuration(ms,resultado, userId)
        return dao.retornaPrograma(userId)
    }


    // ------- Playlist -------
    suspend fun getAllPlaylist(userId: String): List<PlaylistEntity>{
        return dao.getAllPlaylist(userId)
    }
    suspend fun searchPlaylist(id: String,userId: String): PlaylistEntity?{
        playApi.getPlaylist(id)?.let {
            val tracks: List<TrackItens> = it.tracks.items
            val duration = getDurationPlaylist(tracks)
            val entity = it.toPlaylist(userId, duration)
            return salvarPlaylist(entity, tracks, userId)
        }?: return null
    }
    private suspend fun salvarPlaylist(
        entity: PlaylistEntity,
        tracks: List<TrackItens>,
        userId: String
    ): PlaylistEntity {
        salvarTracks(tracks, entity.id)

        dao.let{
            val end = it.retornaPrograma(userId).finishTime + entity.duration
            it.salvarPlaylist(entity)
            it.updateDuration(end, userId)
        }
        return entity
    }
    private fun getDurationPlaylist(listaDeTracks: List<TrackItens>): Long{
        var soma: Long = 0
        for (track in listaDeTracks) soma += track.track.durationMs
        return soma
    }
    suspend fun apagarPlaylist(id: String, userId: String) {
        dao.let {
            val playlist = it.getPlaylist(id)
            val finish = it.retornaPrograma(userId).finishTime
            deleteTracks(id)
            it.deletePlaylist(playlist)
            it.updateDuration(finish-playlist.duration, userId)
        }
    }


    // ------- Tracks -------
    private suspend fun salvarTracks(lista: List<TrackItens>, playlistId: String){
        for (track in lista){
            val entity = track.track.toTrackEntity(playlistId)
            dao.salvarTrack(entity)
        }
    }
    private suspend fun deleteTracks(id: String){
        val listTracks = dao.getAllTracksId(id)
        for (track in listTracks){
            dao.deleteTrack(track)
        }
    }
    suspend fun getAllTracks(): List<TrackEntity>{
        return dao.getAllTracks()
    }// TODO atrelar musica ao user, e buscar msuica do user
}