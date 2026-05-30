package com.exerovv.deadpixel.feature.users.domain.model

import com.exerovv.deadpixel.core.network.UserRole

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: UserRole,
    val isActive: Boolean,
    val registeredAt: String?
)
