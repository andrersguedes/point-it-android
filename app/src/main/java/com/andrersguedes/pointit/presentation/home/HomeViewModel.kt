package com.andrersguedes.pointit.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class HomeError {
    EMPTY_FIELDS
}

data class HomeUiState(
    val userName: String = "",
    val taskTitle: String = "",
    val errorType: HomeError? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onUserNameChanged(name: String) {
        _uiState.update { it.copy(userName = name, errorType = null) }
    }

    fun onTaskTitleChanged(title: String) {
        _uiState.update { it.copy(taskTitle = title, errorType = null) }
    }

    fun validateAndProceed(onNavigateToRoom: (String, String) -> Unit) {
        val currentState = _uiState.value

        if (currentState.userName.isBlank() || currentState.taskTitle.isBlank()) {
            _uiState.update { it.copy(errorType = HomeError.EMPTY_FIELDS) }
            return
        }

        onNavigateToRoom(currentState.userName.trim(), currentState.taskTitle.trim())
    }
}