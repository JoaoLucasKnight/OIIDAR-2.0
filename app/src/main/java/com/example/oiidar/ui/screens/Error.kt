package com.example.oiidar.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.oiidar.R

@Composable
fun ErrorScreen() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
        ) {
            Image(
                painter = painterResource(id = R.drawable.oiidar),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.BottomEnd)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Text(text = "Algo deu errado",
                modifier = Modifier.align(Alignment.Center))
        }
    }
}