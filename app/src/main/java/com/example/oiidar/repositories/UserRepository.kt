package com.example.oiidar.repositories



import android.util.Log
import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity

import com.example.oiidar.model.SpotifyUser
import com.example.oiidar.model.TrackItens
import com.example.oiidar.model.toPlaylist
import com.example.oiidar.model.toTrackEntity
import com.example.oiidar.model.toUser
import com.example.oiidar.net.service.PlaylistService
import com.example.oiidar.net.service.UserService

import javax.inject.Inject

const val TAG = "OIIDAR"
class UserRepository @Inject constructor(
    private val dao: Dao,
    private val api: UserService,
    private val playApi: PlaylistService
){
    // ------- User -------
    private suspend fun salvaUser(user: SpotifyUser){
        val entity = user.toUser()
        dao.salvarUser(entity)
        dao.salvarPrograma(
            ProgramaEntity(
                id = entity.nameId,
                tempoInicio = 0,
                tempoFinal = 0
            )
        )
    }
    suspend fun buscaUserLogado(): UserEntity?{
        dao.getUserLogado(true)?.also {
            return it
        }?: run {
            return null
        }
        return null
    }
    suspend fun verificarSeJaEstaSalvo(){
        val resposta = api.getUser()
        dao.getUser(resposta.id)?.let {
            Log.d(TAG, "Salvo? Sim")
            dao.updateStatus(true, resposta.id)
        }?: run {
            Log.d(TAG, "Salvo? Não")
            salvaUser(resposta)
        }
    }

    suspend fun deslogaUser(User: UserEntity){
        dao.updateStatus(false, User.nameId)
    }



    //-------- Programa -------
    suspend fun getPrograma(USER_ID: String): ProgramaEntity{
        return dao.retornaPrograma(USER_ID)
    }
    suspend fun atualizatempoInicio(ms: Long,USER_ID: String ): ProgramaEntity{
        val tempoInicio = dao.retornaPrograma(USER_ID).tempoInicio
        val tempoFinal = dao.retornaPrograma(USER_ID).tempoFinal
        val resultado = (ms - tempoInicio)+tempoFinal

        dao.updateTempoInicio(ms, USER_ID)
        dao.updateTempoFinal(resultado, USER_ID)
        return dao.retornaPrograma(USER_ID)
    }



    // ------- Playlist -------
    suspend fun getAllPlaylist(USER_ID: String): List<PlaylistEntity>{
        return dao.getAllPlaylist(USER_ID)
    }
    suspend fun salvarPlaylist(id: String, USER_ID: String): PlaylistEntity {
        // web playlits
        val resposta = playApi.getPlaylist(id)

        // converte em entidade
        val entity = resposta.toPlaylist(USER_ID)

        // pega a lista de musicas
        val tracks: List<TrackItens> = resposta.tracks.items

        // o tempo total da Programação
        val tempoFinal: Long = dao.retornaPrograma(USER_ID).tempoFinal
        val duracao = entity.soma


        // salva Playlist e tracks
        dao.salvarPlaylist(entity)
        salvarTracks(tracks, entity.id)

        //atualiza o tempo final
        dao.updateTempoFinal(tempoFinal + duracao, USER_ID)

        return entity
    }
    suspend fun apagarPlaylist(id: String, USER_ID: String) {
        val tempo: Long = dao.retornaPrograma(USER_ID).tempoFinal
        val playlist = dao.getPlaylist(id)

        deleteTracks(id)
        dao.updateTempoFinal(tempo - playlist.soma, USER_ID)
        dao.deletePlaylist(playlist)
    }



    // ------- Tracks -------
    suspend fun salvarTracks(lista: List<TrackItens>, playlistId: String){
        for (track in lista){
            val entity = track.track.toTrackEntity(playlistId)
            dao.salvarTrack(entity)
        }
    }
    suspend fun deleteTracks(id: String){
        val listaTracks = dao.getAllTracksId(id)
        for (track in listaTracks){
            dao.deleteTrack(track)
        }
    }
    suspend fun getAllTracks(): List<TrackEntity>{
        return dao.getAllTracks()
    }
}