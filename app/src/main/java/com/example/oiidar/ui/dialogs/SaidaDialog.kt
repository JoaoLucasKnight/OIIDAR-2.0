package com.example.oiidar.ui.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.oiidar.ui.theme.OIIDARTheme

@Composable
fun SaidaDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
){
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error)
        },
        title = {
            Text(
                text = "Atenção"
            )
        },
        text = {
            Text(
                text = "Deseja realmente sair?"
            )
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text("Sim")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest()}
            ){
            Text("Não")
            }
        }
    )
}

@Preview
@Composable
fun SaidaDialogPreview(){

    OIIDARTheme {
        Surface {
        }
    }

}