package com.example.oiidar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val nameId: String,
    val img: String,
    val status: Boolean
)
