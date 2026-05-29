package com.exerovv.deadpixel.core.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getAccessToken()
        val request = chain.request().newBuilder()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()
        val response = chain.proceed(request)
        if (response.code == 401 && token != null) {
            tokenManager.signalUnauthorized()
        }
        return response
    }
}
