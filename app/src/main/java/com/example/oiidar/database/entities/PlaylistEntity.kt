package com.example.oiidar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistEntity(

    val userId: String,
    @PrimaryKey
    val id: String,
    val img: String,
    val name: String,
    val uri: String,
    val duration: Long
)
