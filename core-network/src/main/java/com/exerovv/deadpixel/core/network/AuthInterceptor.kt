package com.exerovv.deadpixel.core.network

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefresher: TokenRefresher
) : Interceptor {

    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getAccessToken()
        val request = chain.request().newBuilder()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()
        val response = chain.proceed(request)

        if (response.code != 401 || token == null) return response

        val newToken = runBlocking {
            mutex.withLock {
                val currentToken = tokenManager.getAccessToken()
                if (currentToken != null && currentToken != token) {
                    return@withLock currentToken
                }

                val refreshToken = tokenManager.getRefreshToken()
                    ?: return@withLock run { tokenManager.signalUnauthorized(); null }

                val tokens = tokenRefresher.refresh(refreshToken)
                if (tokens != null) {
                    tokenManager.saveTokens(tokens.first, tokens.second)
                    tokens.first
                } else {
                    tokenManager.signalUnauthorized()
                    null
                }
            }
        }

        return if (newToken != null) {
            response.close()
            chain.proceed(request.newBuilder().header("Authorization", "Bearer $newToken").build())
        } else {
            response
        }
    }
}
