package com.andrersguedes.pointit.presentation.room

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Participant(
    val id: String,
    val name: String,
    val selectedVote: String? = null,
    val hasVoted: Boolean = false
)

data class RoomUiState(
    val userName: String = "",
    val taskTitle: String = "",
    val participants: List<Participant> = emptyList(),
    val currentSelectedVote: String? = null,
    val areVotesRevealed: Boolean = false,
    val averageScore: Float? = null
)

class RoomViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    fun initSession(userName: String, taskTitle: String) {
        // Simulando a entrada do usuário e alguns membros na sala para demonstração ágil
        val initialParticipants = listOf(
            Participant(id = "1", name = userName, selectedVote = null, hasVoted = false),
            Participant(id = "2", name = "Ana Tech Lead", selectedVote = "5", hasVoted = true),
            Participant(id = "3", name = "Lucas Dev", selectedVote = "8", hasVoted = true),
            Participant(id = "4", name = "Thiago Dev", selectedVote = "5", hasVoted = true),
            Participant(id = "5", name = "Clara Dev", selectedVote = "3", hasVoted = true)
        )

        _uiState.update {
            it.copy(
                userName = userName,
                taskTitle = taskTitle,
                participants = initialParticipants
            )
        }
    }

    fun selectVote(vote: String) {
        _uiState.update { currentState ->
            val updatedParticipants = currentState.participants.map { participant ->
                if (participant.name == currentState.userName) {
                    participant.copy(selectedVote = vote, hasVoted = true)
                } else {
                    participant
                }
            }
            currentState.copy(
                currentSelectedVote = vote,
                participants = updatedParticipants
            )
        }
    }

    fun revealVotes() {
        _uiState.update { currentState ->
            val numericVotes = currentState.participants
                .mapNotNull { it.selectedVote?.toIntOrNull() }

            val average = if (numericVotes.isNotEmpty()) numericVotes.average().toFloat() else null

            currentState.copy(
                areVotesRevealed = true,
                averageScore = average
            )
        }
    }

    fun resetVoting() {
        _uiState.update { currentState ->
            val resetParticipants = currentState.participants.map {
                it.copy(selectedVote = null, hasVoted = false)
            }
            currentState.copy(
                currentSelectedVote = null,
                areVotesRevealed = false,
                averageScore = null,
                participants = resetParticipants
            )
        }
    }
}