package com.example.oiidar.ui.screens

import android.util.Log
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oiidar.R
import com.example.oiidar.ui.theme.OIIDARTheme
import com.example.oiidar.ui.viewModel.AuthVM
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun Logar(
    viewModel: AuthVM,
    authInit: () -> Unit
){

    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
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
                        .align(Alignment.BottomStart)
                )
            }
            Box {
                Column(
                    modifier = Modifier
                        .padding(32.dp, 16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            authInit()
                        },
                        modifier = Modifier
                            .padding(top = 32.dp)

                    ) {
                        Text(
                            text = "Conectar Ao Spotify",
                            style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

    }

}
@Preview(showSystemUi = true)
@Composable
private fun LogarPreview(){
    OIIDARTheme{

    }

}