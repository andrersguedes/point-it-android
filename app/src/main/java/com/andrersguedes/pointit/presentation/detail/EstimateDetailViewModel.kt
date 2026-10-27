package com.andrersguedes.pointit.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import com.andrersguedes.pointit.data.repository.EstimateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstimateDetailUiState(
    val estimate: EstimateEntity? = null,
    val isLoading: Boolean = true
)

class EstimateDetailViewModel(
    private val repository: EstimateRepository,
    private val estimateId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstimateDetailUiState())
    val uiState: StateFlow<EstimateDetailUiState> = _uiState.asStateFlow()

    init {
        loadEstimateDetails()
    }

    private fun loadEstimateDetails() {
        viewModelScope.launch {
            val estimate = repository.getEstimateById(estimateId)
            _uiState.update {
                it.copy(estimate = estimate, isLoading = false)
            }
        }
    }
}