package com.example.oiidar.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oiidar.ui.uiStates.HomeState

@Composable
fun Body(
    pad: PaddingValues,
    nav: (String) -> Unit,
    state: HomeState
){
    val program = state.program
    val status = state.status
    val music = state.track

    val fatherModifier = Modifier.fillMaxWidth().padding(24.dp, 24.dp, 24.dp, 8.dp)
    Column(Modifier.padding(pad)){
        Clock(modifier = fatherModifier)
        ProgramComponent(modifier = fatherModifier, nav = {nav(it)}, program = program )
        Music(modifier = fatherModifier, status = status , music = music, ms = state.ms )
    }
}
@Preview
@Composable
fun BodyPreview() {
    Body(pad = PaddingValues(0.dp), nav = {}, state = HomeState())
}