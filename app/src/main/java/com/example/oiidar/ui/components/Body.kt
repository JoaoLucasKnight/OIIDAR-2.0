package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oiidar.convertType.toHoras
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.model.Images
import com.example.oiidar.navigation.Destination

@Composable
fun Body(
    pad: PaddingValues,
    nav: (String) -> Unit,
    program: ProgramaEntity?,
    status: Boolean,
    music: TrackEntity?,
){
    val fatherModifier = Modifier.fillMaxWidth().padding(24.dp, 24.dp, 24.dp, 8.dp)
    Column(Modifier.padding(pad)){
        Clock(modifier = fatherModifier)
        ProgramComponent(modifier = fatherModifier, nav = {nav(it)}, program = program )
        Music(modifier = fatherModifier, status = status , music = music )
    }
}
@Preview
@Composable
fun BodyPreview() {
    Body(
        pad = PaddingValues(0.dp),
        nav = {},
        program = ProgramaEntity("id", 10000000, 50000000),
        status = true,
        music = TrackEntity("test", "teste","test", "test", "" , 10000000 )
    )
}