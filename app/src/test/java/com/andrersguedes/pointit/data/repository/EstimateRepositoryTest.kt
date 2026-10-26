package com.andrersguedes.pointit.data.repository

import com.andrersguedes.pointit.data.local.dao.EstimateDao
import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EstimateRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var estimateDao: EstimateDao

    private lateinit var repository: EstimateRepository

    @Before
    fun setup() {
        repository = EstimateRepository(estimateDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun whenSavingEstimateShouldMapAndCallInsertEstimateInDao() = runTest {
        val slot = slot<EstimateEntity>()
        coEvery { estimateDao.insertEstimate(capture(slot)) } returns Unit

        repository.insertEstimate(
            taskTitle = "[PROJ-101] Refatorar Room",
            userName = "André",
            finalAverage = 8.0f
        )

        coVerify(exactly = 1) { estimateDao.insertEstimate(any()) }

        val capturedEntity = slot.captured
        assertEquals("[PROJ-101] Refatorar Room", capturedEntity.taskTitle)
        assertEquals("André", capturedEntity.userName)
        assertEquals(8.0f, capturedEntity.finalAverage)
    }

    @Test
    fun whenGettingAllEstimatesShouldCallGetAllEstimatesInDao() = runTest {
        val mockEntities = listOf(
            EstimateEntity(id = 1, taskTitle = "Task A", userName = "André", finalAverage = 5.0f),
            EstimateEntity(id = 2, taskTitle = "Task B", userName = "Ana", finalAverage = 13.0f)
        )
        coEvery { estimateDao.getAllEstimates() } returns flowOf(mockEntities)

        val result = repository.getAllEstimates().first()

        assertEquals(2, result.size)
        assertEquals("Task A", result[0].taskTitle)
        assertEquals(13.0f, result[1].finalAverage)
    }

    @Test
    fun whenUpdatingEstimateShouldMapAndCallUpdateEstimateInDao() = runTest {
        coEvery { estimateDao.clearHistory() } returns Unit

        repository.clearHistory()

        coVerify(exactly = 1) { estimateDao.clearHistory() }
    }

}