package com.example.oiidar


import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.rememberNavController
import com.example.oiidar.conectionApi.AuthInterceptor
import com.example.oiidar.conectionApi.Authy
import com.example.oiidar.conectionApi.Spotify
import com.example.oiidar.di.modules.AuthorizationResultContract
import com.example.oiidar.navigation.OiidarNavHost
import com.example.oiidar.ui.theme.OIIDARTheme
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val TAG = "OIIDAR"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: (ActivityResultLauncher<AuthorizationRequest>) -> Unit
    private lateinit var launcher: ActivityResultLauncher<AuthorizationRequest>

    @Inject
    lateinit var authInterceptor: AuthInterceptor



    //    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OIIDARTheme {
                launcher = registerForActivityResult(AuthorizationResultContract()) { response ->
                    when(response.type){
                        AuthorizationResponse.Type.TOKEN -> {
                            // Tratar resposta bem-sucedida
                            authInterceptor.pegaToken(response.accessToken)
                            Log.d(TAG, "onCreate: ${response.accessToken}")
                        }
                        AuthorizationResponse.Type.ERROR -> {
                            // Tratar resposta de erro
                            Log.d(TAG, "onCreate: ${response.error}")
                        }
                        else -> {
                            // outros
                        }
                    }
                }
                val navController = rememberNavController()
                OiidarNavHost(
                    navController = navController,
                    authy = { auth(launcher)}
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

}



