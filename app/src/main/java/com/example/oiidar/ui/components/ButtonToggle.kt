package com.example.oiidar.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.oiidar.ui.theme.OIIDARTheme

@Composable
fun ButtonToggle(
    checked: Boolean,
    toggle: () -> Unit,
    text: String
){
    val color = if (checked) { MaterialTheme.colorScheme.primary
        } else { MaterialTheme.colorScheme.onTertiaryContainer }
    Button(
        onClick = { toggle() },
        colors = ButtonDefaults.buttonColors(color)
    ){
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview
@Composable
fun ButtonTogglePreview(){
    OIIDARTheme {
        ButtonToggle(checked = true, toggle = {}, "oi")
    }
}

@Preview
@Composable
fun ButtonToggleFalsePreview(){
    OIIDARTheme {
        ButtonToggle(checked = false, toggle = {}, "oi")
    }
}