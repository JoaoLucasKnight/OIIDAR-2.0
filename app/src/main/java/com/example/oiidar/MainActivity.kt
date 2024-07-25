package com.example.oiidar


import android.app.Activity
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
import com.example.oiidar.authenticator.TokenProvider
import com.example.oiidar.conectionApi.Spotify
import com.example.oiidar.contantes.CLIENT_ID
import com.example.oiidar.contantes.REDIRECT_URI
import com.example.oiidar.navigation.OiidarNavHost
import com.example.oiidar.ui.theme.OIIDARTheme
import com.example.oiidar.ui.viewModel.AuthVM
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val TAG = "OIIDAR"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenProvider: TokenProvider

    private lateinit var authRes: ActivityResultLauncher<Intent>

    private val vm: AuthVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authRes = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val response = AuthorizationClient.getResponse(result.resultCode, result.data)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val accessToken = response.accessToken
                    Log.d("OIIDAR", "Access Token: $accessToken")
                    tokenProvider.setToken(accessToken)
                    vm.checkSaveOrSave()
                }
                AuthorizationResponse.Type.ERROR -> { Log.e("OIIDAR", "Error: ${response.error}") }
                else -> { Log.d("OIIDAR", "Auth result: ${response.type}") }
            }

        }
        enableEdgeToEdge()
        setContent {
            OIIDARTheme {
                val navController = rememberNavController()
                OiidarNavHost(
                    vm = vm,
                    navController = navController,
                    authInit = { authInit(authRes, this) }
                )
            }
        }


    }
    private fun authInit(launcher: ActivityResultLauncher<Intent> ,activity: Activity){
        Log.d("OIIDAR", "authInit")
        val builder: AuthorizationRequest.Builder = AuthorizationRequest
            .Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
            .setScopes(arrayOf("user-read-private", "user-read-email"))
        val request: AuthorizationRequest = builder.build()
        val intent = AuthorizationClient.createLoginActivityIntent(activity,request)
        launcher.launch(intent)
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



