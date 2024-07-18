package com.example.oiidar.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity

@Dao
interface Dao {

    // ------ Usuarios --------
    @Insert
    suspend fun saveUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE nameId = :userid")
    suspend fun getUser(userid: String): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE status = :status")
    suspend fun getUserLogIn(status: Boolean): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE nameId = :userid")
    suspend fun returnUser(userid: String): UserEntity

    @Query("UPDATE UserEntity SET status = :status WHERE nameId = :userid")
    suspend fun updateStatus(status: Boolean, userid: String)

    // ------ Programas --------
    @Insert
    suspend fun saveProgram(programa: ProgramaEntity)

    @Query("UPDATE ProgramaEntity SET startTime = :start WHERE id = :id")
    suspend fun updateStartDuration(start: Long,  id: String)

    @Query("UPDATE ProgramaEntity SET finishTime = :finish WHERE id = :id")
    suspend fun updateFinishDuration(finish: Long, id: String)

    @Query("SELECT * FROM ProgramaEntity WHERE id = :programaid")
    suspend fun getProgram(programaid: String): ProgramaEntity

    // ------ Playlist  --------
    @Insert
    suspend fun savePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM PlaylistEntity WHERE userId= :userId")
    suspend fun getPlaylists(userId: String): List<PlaylistEntity>

    @Query("SELECT * FROM PlaylistEntity WHERE id = :id")
    suspend fun getPlaylist(id: String): PlaylistEntity
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    // ------ Tracks --------
    @Insert
    suspend fun saveTrack(track: TrackEntity)
    @Delete
    suspend fun deleteTrack(track: TrackEntity)
    @Query("SELECT * FROM TrackEntity WHERE playlistId = :id" )
    suspend fun getTracksPlaylist(id: String): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity " )
    suspend fun getAllTracks(): List<TrackEntity>

}