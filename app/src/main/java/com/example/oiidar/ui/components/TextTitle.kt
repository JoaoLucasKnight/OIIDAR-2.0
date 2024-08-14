package com.example.oiidar.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TextTitle(text: String){
    Text(
        text = text,
        textAlign = TextAlign.End,
        style = MaterialTheme.typography.headlineMedium
    )
}