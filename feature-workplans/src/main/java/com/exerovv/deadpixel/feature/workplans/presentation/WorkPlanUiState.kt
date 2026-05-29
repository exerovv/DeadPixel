package com.exerovv.deadpixel.feature.workplans.presentation

import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlan

sealed interface WorkPlanUiState {
    data object Loading : WorkPlanUiState
    data object NotFound : WorkPlanUiState
    data class Success(val plan: WorkPlan) : WorkPlanUiState
    data class Error(val message: String) : WorkPlanUiState
}
