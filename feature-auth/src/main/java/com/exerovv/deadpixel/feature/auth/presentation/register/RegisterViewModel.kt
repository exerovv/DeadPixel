package com.exerovv.deadpixel.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: UserRole = UserRole.MASTER,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed interface RegisterCommand {
    data class UpdateName(val name: String) : RegisterCommand
    data class UpdateEmail(val email: String) : RegisterCommand
    data class UpdatePassword(val password: String) : RegisterCommand
    data class UpdateRole(val role: UserRole) : RegisterCommand
    data object Submit : RegisterCommand
    data object ResetSuccess : RegisterCommand
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun processCommand(command: RegisterCommand) {
        when (command) {
            is RegisterCommand.UpdateName -> _state.update { it.copy(name = command.name) }
            is RegisterCommand.UpdateEmail -> _state.update { it.copy(email = command.email) }
            is RegisterCommand.UpdatePassword -> _state.update { it.copy(password = command.password) }
            is RegisterCommand.UpdateRole -> _state.update { it.copy(role = command.role) }
            RegisterCommand.Submit -> register()
            RegisterCommand.ResetSuccess -> _state.update { it.copy(isSuccess = false) }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val s = _state.value
            when (val result = registerUseCase(s.name, s.email, s.password, s.role.name)) {
                is ApiResult.Success -> _state.update { it.copy(isLoading = false, isSuccess = true) }
                is ApiResult.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message ?: "Ошибка регистрации")
                }
            }
        }
    }
}
