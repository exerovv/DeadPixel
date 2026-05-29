package com.exerovv.deadpixel.feature.auth.data.remote

import com.exerovv.deadpixel.core.network.TokenRefresher
import com.exerovv.deadpixel.feature.auth.data.remote.dto.RefreshTokenRequest
import javax.inject.Inject

class TokenRefresherImpl @Inject constructor(
    private val authApiService: AuthApiService
) : TokenRefresher {

    override suspend fun refresh(refreshToken: String): Pair<String, String>? =
        runCatching { authApiService.refresh(RefreshTokenRequest(refreshToken)) }
            .getOrNull()?.let { Pair(it.token, it.refreshToken) }
}
