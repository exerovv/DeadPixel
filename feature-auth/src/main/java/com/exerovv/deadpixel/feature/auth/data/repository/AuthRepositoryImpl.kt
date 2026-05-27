package com.exerovv.deadpixel.feature.auth.data.repository

import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.feature.auth.data.remote.AuthApiService
import com.exerovv.deadpixel.feature.auth.data.remote.dto.LoginRequest
import com.exerovv.deadpixel.feature.auth.data.remote.dto.RefreshTokenRequest
import com.exerovv.deadpixel.feature.auth.data.remote.dto.RegisterRequest
import com.exerovv.deadpixel.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String) {
        val response = apiService.login(LoginRequest(email, password))
        tokenManager.saveTokens(response.token, response.refreshToken)
    }

    override suspend fun register(name: String, email: String, password: String, role: String) {
        val response = apiService.register(RegisterRequest(name, email, password, role))
        tokenManager.saveTokens(response.token, response.refreshToken)
    }

    override suspend fun logout() {
        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken != null) {
            runCatching { apiService.logout(RefreshTokenRequest(refreshToken)) }
        }
        tokenManager.clearTokens()
    }

    override fun isLoggedIn(): Boolean = tokenManager.getAccessToken() != null
}
