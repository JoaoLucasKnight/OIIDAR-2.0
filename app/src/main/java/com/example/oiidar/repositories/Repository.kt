package com.example.oiidar.repositories

import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity
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
    private suspend fun saveUser(user: UserEntity){
        dao.saveUser(user)
    }
    suspend fun userLogIn(): UserEntity? {
        return dao.getUserLogIn()
    }
    suspend fun checkUserSave(user: UserEntity): UserEntity? {
        return dao.getUser(user.nameId)
    }
    suspend fun updateStatusUser(status: Boolean, user: UserEntity){
        dao.updateStatus(status, user.nameId)
    }

    suspend fun getSpotifyUser(): UserEntity{
        return api.getUser().toUser()
    }
    // TODO request Api test

    //          --------- Program ---------
    suspend fun getProgram(user: UserEntity): ProgramaEntity{
        return dao.getProgram(user.nameId)
    }
    private suspend fun saveProgram(entity: UserEntity){
        dao.saveProgram(ProgramaEntity(entity.nameId))
    }
    private suspend fun updateFinishProgram(duration: Long, program: ProgramaEntity){
        dao.updateFinishDuration(duration, program.id)
    }
    suspend fun updateStartProgram(start: Long, user: UserEntity){
        dao.updateStartDuration(start, user.nameId)
        updateProgram(user)
    }

    // ------- Playlist -------
    private suspend fun getPlaylist(idPlaylist: String): PlaylistEntity{
        return dao.getPlaylist(idPlaylist)
    }
    suspend fun getListPlaylists(user: UserEntity): List<PlaylistEntity>{
        return dao.getListPlaylists(user.nameId)
    }
    private suspend fun savePlaylist(entity: PlaylistEntity){
        dao.savePlaylist(entity)
    }
    private suspend fun deletePlaylist(entity: PlaylistEntity) {
        dao.deletePlaylist(entity)
    }
    private suspend fun responsePlaylist(idPlaylist: String): SpotifyPlaylist{
        return playApi.getPlaylist(idPlaylist)
    }
    // TODO request Api test


    // ------- Tracks -------
    private suspend fun saveTrack(entity: TrackEntity){
        dao.saveTrack(entity)
    }
    private suspend fun deleteTrack(track: TrackEntity){
        dao.deleteTrack(track)
    }
    private suspend fun getTracksPlaylist(idPlaylist: String): List<TrackEntity>{
        return dao.getTracksPlaylist(idPlaylist)
    }
    suspend fun getTracksUser(user : UserEntity): List<TrackEntity>{
        val listPlaylist = getListPlaylists(user)
        val list: MutableList<TrackEntity> = mutableListOf()
        for (playlist in listPlaylist){
            val music = dao.getTracksPlaylist(playlist.id)
            list.addAll(music)
        }
        return list
    }

    // --------- Logic ---------ProgramViewModel
    suspend fun searchAndSave(idPlaylist: String, user: UserEntity){
        val spotifyPlaylist = responsePlaylist(idPlaylist)
        val list = spotifyPlaylist.getListTrackItems()
        var duration: Long = 0
        for (track in list){
            val entity = spotifyPlaylist.getTrackEntity(track, idPlaylist)
            duration += entity.duration
            saveTrack(entity)
        }
        val entity = spotifyPlaylist.toPlaylistEntity(user.nameId, duration)
        savePlaylist(entity)
    }
    suspend fun removePlaylistAndTrack(idPlaylist: String){
        val listTrack = getTracksPlaylist(idPlaylist)
        for (track in listTrack){ deleteTrack(track) }
        deletePlaylist(getPlaylist(idPlaylist))
    }
    suspend fun updateProgram( user: UserEntity){
        val list = getListPlaylists(user)
        val program = getProgram(user)
        var duration: Long = program.startTime
        for (playlist in list){
            duration += playlist.duration
        }
        updateFinishProgram(duration, program)
    }

    // --------- Logic --------- MainViewModel
    suspend fun saveUserAndProgram(user: UserEntity){
        saveUser(user)
        saveProgram(user)
        updateStatusUser(true, user)
    }
}


