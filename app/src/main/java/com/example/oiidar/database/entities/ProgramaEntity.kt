package com.example.oiidar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProgramaEntity(
    @PrimaryKey
    val id: String ,
    val startTime: Long = 0,
    val finishTime: Long = 0,
)
