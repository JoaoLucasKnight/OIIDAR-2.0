package com.example.oiidar.model

data class Tracks(
    val next: String,
    val previous: Int?,
    val total: Int,
    val offset: Int,
    val items: List<TrackItens>
)

