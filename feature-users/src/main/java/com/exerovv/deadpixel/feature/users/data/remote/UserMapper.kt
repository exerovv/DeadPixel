package com.exerovv.deadpixel.feature.users.data.remote

import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.users.data.remote.dto.UserDto
import com.exerovv.deadpixel.feature.users.domain.model.User

fun UserDto.toDomain() = User(
    id = id,
    name = name,
    email = email,
    role = runCatching { UserRole.valueOf(role) }.getOrDefault(UserRole.MASTER),
    isActive = isActive,
    registeredAt = registeredAt
)
