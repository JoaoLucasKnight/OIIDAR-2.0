package com.example.oiidar.di.modules

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.oiidar.contantes.CLIENT_ID
import com.example.oiidar.contantes.REDIRECT_URI
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
object Auth {
    @Provides
    fun provideAuth(): (ActivityResultLauncher<AuthorizationRequest>) -> Unit  {
        return {launcher: ActivityResultLauncher<AuthorizationRequest> ->
            val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
            builder.setScopes(arrayOf("user-read-private", "user-read-email"))
            val request = builder.build()
            launcher.launch(request)
        }
    }
}
class AuthorizationResultContract(): ActivityResultContract<AuthorizationRequest, AuthorizationResponse>() {
    override fun createIntent(context: Context, input: AuthorizationRequest): Intent {
        return AuthorizationClient.createLoginActivityIntent(context as Activity?, input)
    }
    override fun parseResult(resultCode: Int, intent: Intent?): AuthorizationResponse {
        return AuthorizationClient.getResponse(resultCode, intent)
    }
}
