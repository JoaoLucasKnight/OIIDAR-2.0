package com.example.oiidar.model

import com.google.gson.annotations.SerializedName


data class Track(
    @SerializedName("duration_ms")
    val durationMs: Long,
    val album: Album,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)




