package com.example.oiidar.model

class Horas(
    val horas: Long,
    val minutos: Long,
    val segundos: Long
){
    override fun toString(): String {
        return String.format("%02d:%02d", horas, minutos)
    }
}


