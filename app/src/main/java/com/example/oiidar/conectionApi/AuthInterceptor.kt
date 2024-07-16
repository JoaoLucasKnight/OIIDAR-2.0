package com.example.oiidar.conectionApi

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }

    fun pegaToken(onToken: String){
        token = onToken
        Log.d("token", "Passou o token para o AuthInterceptor")
    }
}