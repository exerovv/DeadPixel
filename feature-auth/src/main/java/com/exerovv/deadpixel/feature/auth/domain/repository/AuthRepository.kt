package com.exerovv.deadpixel.feature.auth.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun register(name: String, email: String, password: String, role: String)
    suspend fun logout()
    fun isLoggedIn(): Boolean
}
