package com.exerovv.deadpixel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.auth.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    tokenManager: TokenManager
) : ViewModel() {

    val userId: Int? = tokenManager.getUserId()
    val userRole: UserRole? = tokenManager.getUserRole()

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }
}
