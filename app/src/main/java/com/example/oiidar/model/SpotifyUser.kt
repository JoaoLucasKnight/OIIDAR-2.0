package com.example.oiidar.model

import com.example.oiidar.database.entities.UserEntity

data class SpotifyUser(
    val display_name: String,
    val external_urls: ExternalUrls,
    val href: String?,
    val id: String,
    val images: List<Images>,
    val type: String,
    val uri: String,
    val followers: Followers
)
    fun SpotifyUser.toUser(): UserEntity{
        return UserEntity(
            nameId = id,
            img = images.last().url,
            status = true
        )
    }



