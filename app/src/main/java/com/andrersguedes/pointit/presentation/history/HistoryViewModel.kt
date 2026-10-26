package com.andrersguedes.pointit.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import com.andrersguedes.pointit.data.repository.EstimateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HistoryUiState(
    val estimates: List<EstimateEntity> = emptyList(),
    val isLoading: Boolean = false
)

class HistoryViewModel(
    private val repository: EstimateRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = repository.getAllEstimates()
        .map { estimates ->
            HistoryUiState(estimates = estimates, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryUiState(isLoading = true)
        )

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}