package com.example.oiidar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.oiidar.authenticator.AuthRegistry
import com.example.oiidar.authenticator.TokenProvider
import com.example.oiidar.conectionApi.Spotify
import com.example.oiidar.contantes.TAG
import com.example.oiidar.navigation.NavHost
import com.example.oiidar.ui.theme.OIIDARTheme
import com.example.oiidar.ui.viewModel.MainViewModel
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenProvider: TokenProvider

    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val response = AuthorizationClient.getResponse(result.resultCode, result.data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    tokenProvider.setToken(response.accessToken)
                    viewModel.getAuth(true)
                    Log.d(TAG, "token: ${response.accessToken}")
                }
                else -> { Log.d(TAG, "error") }
            }
        }
        enableEdgeToEdge()
        setContent {
            OIIDARTheme {
                val navController = rememberNavController()
                NavHost(
                    viewModel = viewModel,
                    navController = navController,
                    authInit = { getAuth() }
                )
            }
        }
    }

    private fun getAuth(){
        val intent = AuthRegistry.intent(this)
        launcher.launch(intent)
    }
    override fun onStart() {
        super.onStart()
        Spotify.desconectar()
        Spotify.Conectar(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        Spotify.desconectar()
    }
}



