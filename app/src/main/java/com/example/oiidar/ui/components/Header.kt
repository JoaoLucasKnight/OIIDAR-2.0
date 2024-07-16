package com.example.oiidar.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.oiidar.R
import com.example.oiidar.ui.dialogs.SaidaDialog
import com.example.oiidar.ui.theme.OIIDARTheme

@Composable
fun Header(
    img: String?,
    show: Boolean,
    onShow: (Boolean) -> Unit,
    deslogar: () -> Unit
){
    Row (
        modifier = Modifier.height(120.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.oiidar), contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        TextButton(
            onClick = { onShow(show) }
        ) {
            AsyncImage(
                model = img,
                contentDescription = "Imagem de perfil",
                modifier = Modifier.size(100.dp).clip(CircleShape)
            )
        }
    }
    if (show) {
        SaidaDialog(
            onDismissRequest = { onShow(show) },
            onConfirmation = { deslogar() }
        )
    }
}

@Preview
@Composable
fun HeaderPreview(){
    OIIDARTheme {
        Surface {
        }
    }
}