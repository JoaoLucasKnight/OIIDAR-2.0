package com.example.oiidar.model

import android.annotation.SuppressLint

class Horas(
    val hour: Long = 0,
    val minute: Long = 0,
    val second: Long = 0
){ @SuppressLint("DefaultLocale")
    override fun toString(): String {
        return String.format("%02d:%02d", hour, minute)
    }
}


