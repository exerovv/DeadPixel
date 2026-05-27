package com.exerovv.deadpixel.feature.auth.domain.repository

import com.exerovv.deadpixel.core.network.ApiResult

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<Unit>
    suspend fun register(name: String, email: String, password: String, role: String): ApiResult<Unit>
    suspend fun logout(): ApiResult<Unit>
    fun isLoggedIn(): Boolean
}
