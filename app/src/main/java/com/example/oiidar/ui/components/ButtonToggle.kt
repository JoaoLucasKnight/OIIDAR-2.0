package com.example.oiidar.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.oiidar.ui.theme.OIIDARTheme

@Composable
fun ButtonToggle(
    checked: Boolean,
    toggle: () -> Unit,
    trueAndFalse: Array<String>
){
    val color : Color
    val title : String
    if (checked) {
        color = MaterialTheme.colorScheme.onTertiaryContainer
        title = trueAndFalse[1]
    } else {
        color =  MaterialTheme.colorScheme.primary
        title = trueAndFalse[0]
    }
    Button(
        onClick = { toggle() },
        colors = ButtonDefaults.buttonColors(color)
    ){
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview
@Composable
fun ButtonTogglePreview(){
    OIIDARTheme {
        ButtonToggle(checked = false, toggle = {}, trueAndFalse = arrayOf("oi", "tchau"))
    }
}


@Preview
@Composable
fun ButtonToggleFalsePreview(){
    OIIDARTheme {
        ButtonToggle(checked = true, toggle = {}, arrayOf("oi", "tchau"))
    }
}