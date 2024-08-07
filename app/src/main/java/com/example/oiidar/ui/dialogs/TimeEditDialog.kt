package com.example.oiidar.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput

import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.oiidar.model.Horas
import com.example.oiidar.ui.theme.OIIDARTheme



@Composable
@ExperimentalMaterial3Api
fun TimerEditDialog(
    onDismissRequest: () -> Unit,
    time: Horas,
    salvar: (Horas) -> Unit = {}

) {
    val state = rememberTimePickerState( time.hour.toInt(), time.minute.toInt(), true)

    AlertDialog (
        title = { Text("Editar", style = MaterialTheme.typography.labelMedium) },
        text = {
               TimeInput(state = state)
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { salvar(Horas(state.hour.toLong(), state.minute.toLong(), 0)) }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text("Cancelar")
            }
        }
    )


}


@Preview
@Composable
fun TimerEditorPreview(){
    OIIDARTheme {
    }

}