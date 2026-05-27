package com.exerovv.deadpixel.feature.auth.domain.usecase

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResult<Unit> = authRepository.logout()
}
