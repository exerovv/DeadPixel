package com.exerovv.deadpixel.feature.auth.data.remote

import com.exerovv.deadpixel.feature.auth.data.remote.dto.LoginRequest
import com.exerovv.deadpixel.feature.auth.data.remote.dto.RefreshTokenRequest
import com.exerovv.deadpixel.feature.auth.data.remote.dto.RegisterRequest
import com.exerovv.deadpixel.feature.auth.data.remote.dto.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): TokenResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @POST("api/auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequest): TokenResponse

    @POST("api/auth/logout")
    suspend fun logout(@Body request: RefreshTokenRequest)
}
