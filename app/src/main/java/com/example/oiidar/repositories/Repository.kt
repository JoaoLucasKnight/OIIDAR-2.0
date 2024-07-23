package com.example.oiidar.repositories

import android.util.Log
import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.model.TrackItens
import com.example.oiidar.convertType.toTrackEntity
import com.example.oiidar.convertType.toUser
import com.example.oiidar.model.SpotifyPlaylist
import com.example.oiidar.model.SpotifyUser
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService

import javax.inject.Inject


class Repository @Inject constructor(
    private val dao: Dao,
    private val api: UserService,
    private val playApi: PlaylistService
){
    // ------- User -------
    suspend fun saveUser(user: UserEntity){
        dao.saveUser(user)
    }
    suspend fun userLogIn(): UserEntity? {
        return dao.getUserLogIn()
    }
    suspend fun checkUserSave(id: String): UserEntity? {
        return dao.getUser(id)
    }
    suspend fun updateStatusUser(user: UserEntity){
        dao.updateStatus(!user.status, user.nameId)
    }
    suspend fun getSpotifyUser(): SpotifyUser{
        return api.getUser()
    }// TODO request Api test


    //          --------- Program ---------
    suspend fun getProgram(userId: String): ProgramaEntity{
        return dao.getProgram(userId)
    }
    suspend fun saveProgram(entity: UserEntity){
        dao.saveProgram(ProgramaEntity(entity.nameId))
    }
    suspend fun updateProgram(duration: Long, program: ProgramaEntity){
        dao.updateFinishDuration(duration, program.id)
    }
    suspend fun updateStartProgram(start: Long, id: String){
        dao.updateStartDuration(start, id)
    }


    // ------- Playlist -------
    suspend fun getPlaylist(idPlaylist: String): PlaylistEntity{
        return dao.getPlaylist(idPlaylist)
    }
    suspend fun getPlaylists(entity: UserEntity): List<PlaylistEntity>{
        return dao.getPlaylists(entity.nameId)
    }
    suspend fun savePlaylist(entity: PlaylistEntity){
        dao.savePlaylist(entity)
    }
    suspend fun deletePlaylist(entity: PlaylistEntity) {
        dao.deletePlaylist(entity)
    }
    suspend fun responsePlaylist(idPlaylist: String): SpotifyPlaylist{
        return playApi.getPlaylist(idPlaylist)
    }// TODO request Api test


    // ------- Tracks -------
    suspend fun saveTrack(entity: TrackEntity){
        dao.saveTrack(entity)
    }
    suspend fun deleteTrack(track: TrackEntity){
        dao.deleteTrack(track)
    }
    suspend fun getTracksPlaylist(idPlaylist: String): List<TrackEntity>{
        return dao.getTracksPlaylist(idPlaylist)
    }
    suspend fun getTracksUser(userId : String): List<TrackEntity>{
        val listPlaylist = dao.getPlaylists(userId)
        val list: MutableList<TrackEntity> = mutableListOf()
        for (playlist in listPlaylist){
            val music =dao.getTracksPlaylist(playlist.id)
            list.addAll(music)
        }
        return list
    }
}