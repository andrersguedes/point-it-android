package com.andrersguedes.pointit.presentation.detail

import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import com.andrersguedes.pointit.data.repository.EstimateRepository
import io.mockk.coEvery
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EstimateDetailViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: EstimateRepository

    private lateinit var viewModel: EstimateDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenInitializeWithValidIDShouldLoadEstimateDetailWithSuccess() = runTest {
        val mockEstimate = EstimateEntity(
            id = 10L,
            taskTitle = "[PROJ-500] Teste Detalhes",
            userName = "André",
            finalAverage = 8.0f
        )
        coEvery { repository.getEstimateById(10L) } returns mockEstimate

        viewModel = EstimateDetailViewModel(repository, estimateId = 10L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.estimate)
        assertEquals("[PROJ-500] Teste Detalhes", state.estimate?.taskTitle)
        assertEquals(8.0f, state.estimate?.finalAverage)
    }

    @Test
    fun whenEstimateNotFoundShouldRemainStateNull() = runTest {
        coEvery { repository.getEstimateById(99L) } returns null

        viewModel = EstimateDetailViewModel(repository, estimateId = 99L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.estimate)
    }
}