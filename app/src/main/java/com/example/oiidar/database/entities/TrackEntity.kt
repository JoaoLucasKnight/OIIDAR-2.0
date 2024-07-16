package com.example.oiidar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrackEntity(

    val playlistId: String,
    @PrimaryKey
    val id: String,
    val name: String,
    val img: String,
    val uri: String,
    val duration: Long,

)
