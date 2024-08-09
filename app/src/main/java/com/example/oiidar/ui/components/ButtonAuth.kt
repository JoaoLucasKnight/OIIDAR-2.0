package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oiidar.ui.theme.OIIDARTheme

@Composable
fun ButtonToggle(
    checked: Boolean,
    auth: () -> Unit,
    logIn:() -> Unit
){
   if (checked) {
       Button(
           onClick = {logIn()}, Modifier.padding(top = 32.dp),
           colors = ButtonDefaults
               .buttonColors(containerColor =MaterialTheme.colorScheme.primary)
       ) {
           Text(text = "Entrar na OIIDAR", style = MaterialTheme.typography.bodyLarge)
       }
   } else {
       Button(
           onClick = {auth()}, Modifier.padding(top = 32.dp),
           colors = ButtonDefaults
               .buttonColors(containerColor =MaterialTheme.colorScheme.onTertiaryContainer)
       ) {
           Text(text = "Solocitar Autorização", style = MaterialTheme.typography.bodyLarge)
       }
   }
}

@Preview
@Composable
fun ButtonTogglePreview(){
    OIIDARTheme {
        ButtonToggle(checked = true, auth = {}, logIn = {})
    }
}

@Preview
@Composable
fun ButtonToggleFalsePreview(){
    OIIDARTheme {
        ButtonToggle(checked = false, auth = {}, logIn = {})
    }
}