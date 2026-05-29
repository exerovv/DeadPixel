package com.exerovv.deadpixel.feature.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.users.domain.usecase.GetUsersUseCase
import com.exerovv.deadpixel.feature.users.domain.usecase.SetUserActiveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsers: GetUsersUseCase,
    private val setUserActive: SetUserActiveUseCase,
    tokenManager: TokenManager
) : ViewModel() {

    val isManager: Boolean = tokenManager.getUserRole() == UserRole.MANAGER

    private val _state = MutableStateFlow(UsersUiState())
    val state: StateFlow<UsersUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { getUsers() }
                .onSuccess { users -> _state.update { it.copy(users = users, isLoading = false) } }
                .onFailure { e -> _state.update { it.copy(error = e.message ?: "Ошибка загрузки", isLoading = false) } }
        }
    }

    fun toggleActive(userId: Int, currentValue: Boolean) {
        viewModelScope.launch {
            runCatching { setUserActive(userId, !currentValue) }
                .onSuccess { load() }
                .onFailure { e -> _state.update { it.copy(error = e.message ?: "Ошибка") } }
        }
    }
}
