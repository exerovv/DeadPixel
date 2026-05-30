package com.exerovv.deadpixel.core.network

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        "secure_tokens",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _unauthorizedEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val unauthorizedEvents: SharedFlow<Unit> = _unauthorizedEvents.asSharedFlow()

    fun signalUnauthorized() {
        clearTokens()
        _unauthorizedEvents.tryEmit(Unit)
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit {
            putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
        }
    }

    fun clearTokens() {
        prefs.edit {
            remove("access_token")
                .remove("refresh_token")
        }
    }

    fun getUserId(): Int? {
        val payload = decodePayload() ?: return null
        return Regex(""""userId"\s*:\s*(\d+)""").find(payload)?.groupValues?.get(1)?.toIntOrNull()
    }

    fun getUserRole(): UserRole? {
        val payload = decodePayload() ?: return null
        val raw = Regex(""""role"\s*:\s*"([^"]+)"""").find(payload)?.groupValues?.get(1)
        return raw?.let { runCatching { UserRole.valueOf(it) }.getOrNull() }
    }

    private fun decodePayload(): String? {
        val token = getAccessToken() ?: return null
        return runCatching {
            val encoded = token.split(".").getOrNull(1) ?: return null
            String(android.util.Base64.decode(encoded, android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING))
        }.getOrNull()
    }
}
