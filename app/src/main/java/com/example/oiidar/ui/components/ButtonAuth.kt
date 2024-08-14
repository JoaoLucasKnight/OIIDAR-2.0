package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oiidar.ui.text.AppStrings
import com.example.oiidar.ui.theme.OIIDARTheme

@Composable
fun ButtonAuth(
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
           Text(text = AppStrings.BTT_AUHT_IN, style = MaterialTheme.typography.bodyLarge)
       }
   } else {
       Button(
           onClick = {auth()}, Modifier.padding(top = 32.dp),
           colors = ButtonDefaults
               .buttonColors(containerColor =MaterialTheme.colorScheme.onTertiaryContainer)
       ) {
           Text(text = AppStrings.BTT_AUHT_FIND, style = MaterialTheme.typography.bodyLarge)
       }
   }
}

@Preview
@Composable
fun ButtonTogglePreview(){
    OIIDARTheme {
        ButtonAuth(checked = true, auth = {}, logIn = {})
    }
}

@Preview
@Composable
fun ButtonToggleFalsePreview(){
    OIIDARTheme {
        ButtonAuth(checked = false, auth = {}, logIn = {})
    }
}