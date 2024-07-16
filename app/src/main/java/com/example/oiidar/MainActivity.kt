package com.example.oiidar


import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.oiidar.conectionApi.AuthInterceptor
import com.example.oiidar.conectionApi.Authy
import com.example.oiidar.conectionApi.Spotify
import com.example.oiidar.navigation.OiidarNavHost
import com.example.oiidar.ui.theme.OIIDARTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "OIIDAR"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authInterceptor: AuthInterceptor
    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result -> Authy.result(result.resultCode, result.data, authInterceptor)
        }
        enableEdgeToEdge()
        setContent {
            OIIDARTheme {
                val navController = rememberNavController()
                OiidarNavHost(
                    navController = navController,
                    authy = { autenticar() }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Spotify.desconectar()
        Spotify.Conectar(this)
    }
    override fun onStop() {
        super.onStop()
        Spotify.desconectar()
    }

    fun autenticar(){
        Log.d(TAG, " autenticar() ")
        Authy.authInit(launcher, this)
    }

}



