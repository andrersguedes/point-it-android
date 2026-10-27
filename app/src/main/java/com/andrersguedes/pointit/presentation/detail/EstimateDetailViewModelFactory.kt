package com.andrersguedes.pointit.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andrersguedes.pointit.data.repository.EstimateRepository

class EstimateDetailViewModelFactory(
    private val repository: EstimateRepository,
    private val estimateId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimateDetailViewModel::class.java)) {
            return EstimateDetailViewModel(repository, estimateId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}