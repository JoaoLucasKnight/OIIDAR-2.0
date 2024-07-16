package com.example.oiidar.net.service


import com.example.oiidar.model.SpotifyUser
import retrofit2.http.GET
interface UserService {
    @GET("me")
    suspend fun getUser(): SpotifyUser

}