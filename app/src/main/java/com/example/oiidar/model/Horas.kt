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
fun Horas.ToMs(): Long{
    return horas * 3600000 + minutos * 60000 + segundos * 1000
}

