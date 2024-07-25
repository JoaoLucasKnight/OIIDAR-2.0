package com.example.oiidar.authenticator

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider  ): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        tokenProvider.getToken()?.let {
            builder.header("Authorization", "Bearer $it")
        }
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}