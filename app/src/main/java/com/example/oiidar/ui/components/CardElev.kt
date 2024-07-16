package com.example.oiidar.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.oiidar.R
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.ui.theme.OIIDARTheme

@Composable
fun ElevaCard(
    largura: Int,
    altura: Int,
    musica: TrackEntity
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = largura.dp, height = altura.dp)
    ) {
        AsyncImage(
            model = musica.img,
            contentDescription = "Imagem da musica",
            modifier = Modifier.size(largura.dp)
        )

        if (largura == 140){
            Text(
                text = musica.name,
                modifier = Modifier
                    .padding(8.dp, 16.dp),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }else{
            Text(
                text = musica.name,

                modifier = Modifier
                    .padding(4.dp, 4.dp),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
            )
        }


    }
}

@Preview(showSystemUi = true)
@Composable
fun ElevaCardPreview(){
    OIIDARTheme {
        Row{

        }
    }
}