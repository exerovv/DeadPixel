package com.exerovv.deadpixel.feature.users.presentation

import com.exerovv.deadpixel.feature.users.domain.model.User

data class UsersUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
