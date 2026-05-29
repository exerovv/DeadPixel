package com.exerovv.deadpixel.feature.users.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val isActive: Boolean,
    val registeredAt: String
)
