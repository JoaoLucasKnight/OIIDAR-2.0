package com.example.oiidar.conectionApi

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.example.oiidar.contantes.CLIENT_ID
import com.example.oiidar.contantes.REDIRECT_URI
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class Authy (
    deuCerto: () -> Unit
) {
    companion object {
        fun authInit(launcher: ActivityResultLauncher<Intent> ,activity: Activity){
            Log.d("OIIDAR", "authInit")
            val builder: AuthorizationRequest.Builder = AuthorizationRequest
                .Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(arrayOf("user-read-private", "user-read-email"))
            val request: AuthorizationRequest = builder.build()
            val intent =AuthorizationClient.createLoginActivityIntent(activity,request)
            launcher.launch(intent)
        }
        fun result(resultCode: Int, data: Intent?, authInterceptor: AuthInterceptor) {
            Log.d("OIIDAR", "result")
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("OIIDAR", "TOKEN")
                    authInterceptor.pegaToken(response.accessToken)
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("error", response.error)
                }
                else -> {
                }
            }
        }


    }
}

//fun authInit(launcher: ActivityResultLauncher<Intent>, activity: Activity){
//    val builder: AuthorizationRequest.Builder = AuthorizationRequest
//        .Builder(Authy.CLIENT_ID, AuthorizationResponse.Type.TOKEN, Authy.REDIRECT_URI)
//        .setScopes(arrayOf("user-read-private", "user-read-email"))
//    val request: AuthorizationRequest = builder.build()
//    val intent = AuthorizationClient.createLoginActivityIntent(activity,request)
//    launcher.launch(intent)
//}
//fun reult(resultCode: Int, data: Intent?, authInterceptor: AuthInterceptor) {
//    val response = AuthorizationClient.getResponse(resultCode, data)
//    when (response.type) {
//        AuthorizationResponse.Type.TOKEN -> {
//            authInterceptor.pegaToken(response.accessToken)
//        }
//        AuthorizationResponse.Type.ERROR -> {
//            Log.e("error", response.error)
//        }
//        else -> {
//        }
//    }
//}
//}