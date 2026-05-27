package com.exerovv.deadpixel.feature.auth.data.repository

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.safeApiCall
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

    override suspend fun login(email: String, password: String): ApiResult<Unit> {
        return when (val result = safeApiCall { apiService.login(LoginRequest(email, password)) }) {
            is ApiResult.Success -> {
                tokenManager.saveTokens(result.data.token, result.data.refreshToken)
                ApiResult.Success(Unit)
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun register(name: String, email: String, password: String, role: String): ApiResult<Unit> {
        return when (val result = safeApiCall { apiService.register(RegisterRequest(name, email, password, role)) }) {
            is ApiResult.Success -> {
                tokenManager.saveTokens(result.data.token, result.data.refreshToken)
                ApiResult.Success(Unit)
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun logout(): ApiResult<Unit> {
        val refreshToken = tokenManager.getRefreshToken() ?: run {
            tokenManager.clearTokens()
            return ApiResult.Success(Unit)
        }
        safeApiCall { apiService.logout(RefreshTokenRequest(refreshToken)) }
        tokenManager.clearTokens()
        return ApiResult.Success(Unit)
    }

    override fun isLoggedIn(): Boolean = tokenManager.getAccessToken() != null
}
