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

    // ------ Usuarios --------
    @Insert
    suspend fun salvarUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE nameId = :userid")
    suspend fun getUser(userid: String): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE status = :status")
    suspend fun getUserLogado(status: Boolean): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE nameId = :userid")
    suspend fun retornaUser(userid: String): UserEntity

    @Query("UPDATE UserEntity SET status = :status WHERE nameId = :userid")
    suspend fun updateStatus(status: Boolean, userid: String)

    // ------ Programas --------
    @Insert
    suspend fun salvarPrograma(programa: ProgramaEntity)

    @Query("UPDATE ProgramaEntity SET tempoInicio = :inicio WHERE id = :id")
    suspend fun updateTempoInicio(inicio: Long, id: String)

    @Query("UPDATE ProgramaEntity SET tempoFinal = :fim WHERE id = :id")
    suspend fun updateTempoFinal(fim: Long, id: String)



    @Query("SELECT * FROM ProgramaEntity WHERE id = :programaid")
    suspend fun retornaPrograma(programaid: String): ProgramaEntity

    // ------ Playlist  --------
    @Insert
    suspend fun salvarPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM PlaylistEntity WHERE userId= :userId")
    suspend fun getAllPlaylist(userId: String): List<PlaylistEntity>

    @Query("SELECT * FROM PlaylistEntity WHERE id = :id")
    suspend fun getPlaylist(id: String): PlaylistEntity
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    // ------ Tracks --------
    @Insert
    suspend fun salvarTrack(track: TrackEntity)
    @Delete
    suspend fun deleteTrack(track: TrackEntity)
    @Query("SELECT * FROM TrackEntity WHERE playlistId = :id" )
    suspend fun getAllTracksId(id: String): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity " )
    suspend fun getAllTracks(): List<TrackEntity>

}