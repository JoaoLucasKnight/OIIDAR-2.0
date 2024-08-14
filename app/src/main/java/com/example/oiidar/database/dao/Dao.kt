package com.example.oiidar.database.dao

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

    // ------ User --------
    @Insert
    suspend fun saveUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE nameId = :userid")
    suspend fun getUser(userid: String): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE status = true")
    suspend fun getUserLogIn(): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE nameId = :userid")
    suspend fun returnUser(userid: String): UserEntity

    @Query("UPDATE UserEntity SET status = :status WHERE nameId = :userid")
    suspend fun updateStatus(status: Boolean, userid: String)

    // ------ Programs --------
    @Insert
    suspend fun saveProgram(program: ProgramaEntity)

    @Delete
    suspend fun deleteProgram(program: ProgramaEntity)

    @Query("UPDATE ProgramaEntity SET startTime = :start WHERE id = :id")
    suspend fun updateStartDuration(start: Long,  id: String)

    @Query("UPDATE ProgramaEntity SET finishTime = :finish WHERE id = :id")
    suspend fun updateFinishDuration(finish: Long, id: String)

    @Query("SELECT * FROM ProgramaEntity WHERE id = :programId")
    suspend fun getProgram(programId: String): ProgramaEntity

    // ------ Playlist  --------
    @Insert
    suspend fun savePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM PlaylistEntity WHERE userId= :userId")
    suspend fun getListPlaylists(userId: String): List<PlaylistEntity>

    @Query("SELECT * FROM PlaylistEntity WHERE id = :idPlaylist")
    suspend fun getPlaylist(idPlaylist: String): PlaylistEntity
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    // ------ Tracks --------
    @Insert
    suspend fun saveTrack(track: TrackEntity)
    @Delete
    suspend fun deleteTrack(track: TrackEntity)
    @Query("SELECT * FROM TrackEntity WHERE playlistId = :id" )
    suspend fun getTracksPlaylist(id: String): List<TrackEntity>
}