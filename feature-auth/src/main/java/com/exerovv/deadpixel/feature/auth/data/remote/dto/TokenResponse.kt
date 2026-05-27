package com.exerovv.deadpixel.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(val token: String, val refreshToken: String)
