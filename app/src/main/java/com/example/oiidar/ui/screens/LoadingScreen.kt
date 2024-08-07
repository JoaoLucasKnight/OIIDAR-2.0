package com.example.oiidar.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.oiidar.R
import com.example.oiidar.ui.theme.OIIDARTheme

@Composable
fun LoadingScreen(){
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
            Text(text = "Loading...",
                modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
@Preview
fun LoadingScreenPreview(){
    OIIDARTheme {
        Surface {
            LoadingScreen()
        }
    }

}