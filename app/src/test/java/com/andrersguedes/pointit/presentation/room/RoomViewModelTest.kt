package com.andrersguedes.pointit.presentation.room

import com.andrersguedes.pointit.data.repository.EstimateRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RoomViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: EstimateRepository
    private lateinit var viewModel: RoomViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { repository.insertEstimate(any(), any(), any()) } returns Unit

        viewModel = RoomViewModel(repository)
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
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.areVotesRevealed)
        assertEquals(5.2f, state.averageScore ?: 0f, 0.1f)

        coVerify(exactly = 1) {
            repository.insertEstimate(
                taskTitle = "Task A",
                userName = "André",
                finalAverage = 5.2f
            )
        }
    }

    @Test
    fun whenRevealVotesWithNoVotesShouldCalculateAverageWithOtherParticipantsOnly() = runTest {
        viewModel.initSession(userName = "André", taskTitle = "Task A")

        viewModel.revealVotes()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.areVotesRevealed)
        assertEquals(5.25f, state.averageScore ?: 0f, 0.1f)

        coVerify(exactly = 1) {
            repository.insertEstimate(
                taskTitle = "Task A",
                userName = "André",
                finalAverage = 5.25f
            )
        }
    }

    @Test
    fun whenRevealVotesWithNoValidNumbersShouldCalculateNullAverage() = runTest {
        viewModel.initSession(userName = "André", taskTitle = "Task A")

        // Simulando participantes sem votos numéricos válidos (ex: apenas "?")
        // Vamos forçar o estado para limpar os votos numéricos da simulação padrão se necessário,
        // ou testar resetando e revelando sem votar.
        viewModel.resetVoting()

        viewModel.revealVotes()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.areVotesRevealed)
        assertNull(state.averageScore)

        coVerify(exactly = 0) {
            repository.insertEstimate(any(), any(), any())
        }
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