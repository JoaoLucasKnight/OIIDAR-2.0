package com.example.oiidar.ui.screens

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oiidar.R
import com.example.oiidar.navigation.Destination
import com.example.oiidar.ui.components.ButtonToggle
import com.example.oiidar.ui.viewModel.MainViewModel


@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    authInit: () -> Unit,
    navController: NavController
){
    val user by  viewModel.user.collectAsState()
    val check: Boolean by viewModel.auth.collectAsState()

    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.oiidar),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .align(Alignment.BottomStart)
                )
            }
            Box {           
                Column(
                    modifier = Modifier
                        .padding(32.dp, 16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                   ButtonToggle(
                       checked = check,
                       auth = {authInit()},
                       logIn = { viewModel.checkSaveOrSave()}
                   )
                    LaunchedEffect(key1 = user) {
                        if (user != null){ navController.navigate(Destination.Home.route) }
                    }
                }
            }
        }
    }
}