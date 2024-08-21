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
import com.example.oiidar.R
import com.example.oiidar.navigation.Destination
import com.example.oiidar.ui.components.ButtonToggle
import com.example.oiidar.ui.text.AppStrings
import com.example.oiidar.ui.viewModel.MainViewModel


@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    authInit: () -> Unit,
    nav: (String) -> Unit
){
    val user by  viewModel.user.collectAsState()
    val check: Boolean by viewModel.auth.collectAsState()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = user) {
        if(user != null){
            nav(Destination.Main.route)
        }
    }
    when(state){
        "LOADING" -> {
            Surface {
                LoadingScreen()
                viewModel.checkUser()
            }
        }
        "LOAD" -> {
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
                            val titleBtt = if(check) AppStrings.BTT_AUHT_IN
                            else AppStrings.BTT_AUHT_FIND
                            ButtonToggle(
                                checked = check,
                                toggle = { if (check){ viewModel.checkSaveOrSave()
                                }else{ authInit() } },
                                text = titleBtt
                            )
                        }
                    }
                }
            }
        }
    }
}
