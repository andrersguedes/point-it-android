package com.andrersguedes.pointit.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val userName: String = "",
    val taskTitle: String = "",
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onUserNameChanged(name: String) {
        _uiState.update { it.copy(userName = name, errorMessage = null) }
    }

    fun onTaskTitleChanged(title: String) {
        _uiState.update { it.copy(taskTitle = title, errorMessage = null) }
    }

    fun validateAndProceed(onNavigateToRoom: (String, String) -> Unit) {
        val currentState = _uiState.value

        if (currentState.userName.isBlank() || currentState.taskTitle.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, preencha seu nome e o título da task.") }
            return
        }

        onNavigateToRoom(currentState.userName.trim(), currentState.taskTitle.trim())
    }
}