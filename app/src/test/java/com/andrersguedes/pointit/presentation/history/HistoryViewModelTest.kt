package com.andrersguedes.pointit.presentation.history

import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import com.andrersguedes.pointit.data.repository.EstimateRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: EstimateRepository

    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenInitializeViewModelShouldPopulateStateCorrectly() = runTest {
        val mockEstimates = listOf(
            EstimateEntity(id = 1, taskTitle = "[PROJ-101] Refatorar Room", userName = "André", finalAverage = 8.0f),
            EstimateEntity(id = 2, taskTitle = "[PROJ-102] Testes Unitarios", userName = "Ana", finalAverage = 5.0f)
        )

        // Configura o mock ANTES de qualquer coisa
        coEvery { repository.getAllEstimates() } returns flowOf(mockEstimates)

        // Act - Instancia a ViewModel (dispara o init e o stateIn)
        viewModel = HistoryViewModel(repository)

        // Ativa o fluxo no escopo de teste
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.estimates.size)
        assertEquals("[PROJ-101] Refatorar Room", state.estimates[0].taskTitle)
    }

    @Test
    fun whenRepositoryReturnsEmptyListShouldPopulateStateWithEmptyList() = runTest {
        coEvery { repository.getAllEstimates() } returns flowOf(emptyList())

        viewModel = HistoryViewModel(repository)

        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.estimates.isEmpty())
    }

    @Test
    fun whenCallClearHistoryShouldCallRepositoryClearHistory() = runTest {
        coEvery { repository.getAllEstimates() } returns flowOf(emptyList())
        coEvery { repository.clearHistory() } returns Unit

        viewModel = HistoryViewModel(repository)
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.clearHistory()
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.clearHistory() }
    }

}