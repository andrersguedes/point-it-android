package com.andrersguedes.pointit.presentation.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RoomViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: RoomViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RoomViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenStartSessionShouldPopulateStateCorrectly() = runTest {
        viewModel.initSession(userName = "André", taskTitle = "Refatorar SDK")

        val state = viewModel.uiState.value
        assertEquals("André", state.userName)
        assertEquals("Refatorar SDK", state.taskTitle)
        assertEquals(5, state.participants.size)
        assertEquals("André", state.participants[0].name)
        assertFalse(state.areVotesRevealed)
    }

    @Test
    fun whenVoteSelectedShouldUpdateUserAndParticipantState() = runTest {
        viewModel.initSession(userName = "André", taskTitle = "Task A")

        viewModel.selectVote("8")

        val state = viewModel.uiState.value
        assertEquals("8", state.currentSelectedVote)
        val userParticipant = state.participants.find { it.name == "André" }
        assertEquals("8", userParticipant?.selectedVote)
        assertTrue(userParticipant?.hasVoted == true)
    }

    @Test
    fun whenRevealVotesShouldCalculateAverage() = runTest {
        viewModel.initSession(userName = "André", taskTitle = "Task A")
        viewModel.selectVote("5")

        viewModel.revealVotes()

        val state = viewModel.uiState.value
        assertTrue(state.areVotesRevealed)
        assertEquals(5.2f, state.averageScore ?: 0f, 0.1f)
    }

    @Test
    fun whenRevealVotesWithNoVotesShouldCalculateAverageWithOtherParticipantsOnly() = runTest {
        viewModel.initSession(userName = "André", taskTitle = "Task A")

        viewModel.revealVotes()

        val state = viewModel.uiState.value
        assertTrue(state.areVotesRevealed)
        assertEquals(5.25f, state.averageScore ?: 0f, 0.1f)
    }

    @Test
    fun whenRevealVotesWithNoValidNumbersShouldCalculateNullAverage() = runTest {
        viewModel.initSession(userName = "André", taskTitle = "Task A")

        // Simulando participantes sem votos numéricos válidos (ex: apenas "?")
        // Vamos forçar o estado para limpar os votos numéricos da simulação padrão se necessário,
        // ou testar resetando e revelando sem votar.
        viewModel.resetVoting()

        viewModel.revealVotes()

        val state = viewModel.uiState.value
        assertTrue(state.areVotesRevealed)
        assertNull(state.averageScore)
    }

    @Test
    fun whenResetVotingShouldClearVotesAndHideResults() = runTest {
        viewModel.initSession(userName = "André", taskTitle = "Task A")
        viewModel.selectVote("13")
        viewModel.revealVotes()

        viewModel.resetVoting()

        val state = viewModel.uiState.value
        assertNull(state.currentSelectedVote)
        assertFalse(state.areVotesRevealed)
        assertNull(state.averageScore)

        // Verifica se os participantes voltaram ao estado inicial sem votos
        state.participants.forEach { participant ->
            assertNull(participant.selectedVote)
            assertFalse(participant.hasVoted)
        }
    }

}