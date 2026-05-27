package com.exerovv.deadpixel.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.feature.auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed interface LoginCommand {
    data class UpdateEmail(val email: String) : LoginCommand
    data class UpdatePassword(val password: String) : LoginCommand
    data object Submit : LoginCommand
    data object ResetSuccess : LoginCommand
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun processCommand(command: LoginCommand) {
        when (command) {
            is LoginCommand.UpdateEmail -> _state.update { it.copy(email = command.email) }
            is LoginCommand.UpdatePassword -> _state.update { it.copy(password = command.password) }
            LoginCommand.Submit -> login()
            LoginCommand.ResetSuccess -> _state.update { it.copy(isSuccess = false) }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val s = _state.value
            runCatching { loginUseCase(s.email, s.password) }
                .onSuccess { _state.update { it.copy(isLoading = false, isSuccess = true) } }
                .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.message ?: "Ошибка входа") } }
        }
    }
}
