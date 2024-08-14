package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.example.oiidar.convertType.toHoras
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.navigation.Destination
import com.example.oiidar.ui.text.AppStrings

@Composable
fun ProgramComponent(
    modifier: Modifier,
    nav: (String) -> Unit,
    program: ProgramaEntity?
){
    Column(modifier) {
        TextTitle(text = AppStrings.PROGRAM)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 32.dp, 16.dp, 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            program?.let {
                val startTime = toHoras(it.startTime)
                val finishTime = toHoras(it.finishTime)
                Text(
                    text = "${AppStrings.PROGRAM_START}: $startTime",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${AppStrings.PROGRAM_END}: $finishTime",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { nav(Destination.Program.route) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = AppStrings.PROGRAM)
        }
    }
}
