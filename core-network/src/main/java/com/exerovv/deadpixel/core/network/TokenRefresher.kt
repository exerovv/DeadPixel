package com.exerovv.deadpixel.core.network

interface TokenRefresher {
    suspend fun refresh(refreshToken: String): Pair<String, String>?
}
