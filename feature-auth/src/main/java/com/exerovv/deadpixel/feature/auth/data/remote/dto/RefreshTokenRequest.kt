package com.exerovv.deadpixel.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refreshToken: String)
