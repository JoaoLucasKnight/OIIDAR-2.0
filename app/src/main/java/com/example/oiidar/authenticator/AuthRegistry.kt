package com.example.oiidar.authenticator

import android.app.Activity
import android.content.Intent
import com.example.oiidar.contantes.CLIENT_ID
import com.example.oiidar.contantes.REDIRECT_URI
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

object AuthRegistry {
    private fun createRequest(): AuthorizationRequest {
        val builder: AuthorizationRequest.Builder = AuthorizationRequest
            .Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
            .setScopes(arrayOf("user-read-private", "user-read-email"))
        return  builder.build()
    }
    private fun createIntent(activity: Activity, request: AuthorizationRequest): Intent {
        return AuthorizationClient.createLoginActivityIntent(activity,request)
    }

    fun intent(activity: Activity): Intent {
        return createIntent(activity, createRequest())
    }
}