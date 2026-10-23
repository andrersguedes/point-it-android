package com.andrersguedes.pointit.presentation.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenUserFillsNameAndTaskTitleShouldNavigateWithSuccess() = runTest {
        viewModel.onUserNameChanged("Andre Guedes")
        viewModel.onTaskTitleChanged("Task 123")

        var navigatedSuccessfully = false
        var capturedName = ""
        var capturedTask = ""

        viewModel.validateAndProceed { name, task ->
            navigatedSuccessfully = true
            capturedName = name
            capturedTask = task
        }

        assert(navigatedSuccessfully)
        assertEquals("Andre Guedes", capturedName)
        assertEquals("Task 123", capturedTask)
        assertNull(viewModel.uiState.value.errorType)
    }

    @Test
    fun whenUserFillsOnlyNameShouldShowError() = runTest {
        viewModel.onUserNameChanged("Andre Guedes")
        viewModel.onTaskTitleChanged("")

        var navigatedSuccessfully = false

        viewModel.validateAndProceed { _, _ ->
            navigatedSuccessfully = true
        }

        assert(!navigatedSuccessfully)
        assertEquals(HomeError.EMPTY_FIELDS, viewModel.uiState.value.errorType)
    }

    @Test
    fun whenUserDoesNotFillNameAndTaskTitleShouldShowError() = runTest {
        var navigationSuccessfully = false

        viewModel.validateAndProceed { _, _ ->
            navigationSuccessfully = true
        }

        assert(!navigationSuccessfully)
        assertEquals(HomeError.EMPTY_FIELDS, viewModel.uiState.value.errorType)
    }

}