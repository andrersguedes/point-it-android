package com.andrersguedes.pointit.presentation.history

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import com.andrersguedes.pointit.presentation.theme.PointItTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenHistoryIsClearShouldShowClearStateMessage() {
        val emptyState = HistoryUiState(estimates = emptyList(), isLoading = false)

        composeTestRule.setContent {
            PointItTheme {
                HistoryScreen(
                    state = emptyState,
                    onClearHistory = {},
                    onBackClick = {},
                    onItemClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Nenhuma estimativa salva ainda.").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Limpar Histórico").assertDoesNotExist()
    }

    @Test
    fun whenHasEstimatesShouldShowCardsAndClearButton() {
        val estimates = listOf(
            EstimateEntity(id = 1, taskTitle = "[PROJ-101] Refatorar Room", userName = "André", finalAverage = 8.0f)
        )
        val state = HistoryUiState(estimates = estimates, isLoading = false)

        composeTestRule.setContent {
            PointItTheme {
                HistoryScreen(
                    state = state,
                    onClearHistory = {},
                    onBackClick = {},
                    onItemClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("[PROJ-101] Refatorar Room").assertIsDisplayed()
        composeTestRule.onNodeWithText("Média: 8.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Criador: André").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Limpar Histórico").assertIsDisplayed()
    }

    @Test
    fun whenClickClearHistoryShouldCallItsCallback() {
        var clearClicked = false
        val state = HistoryUiState(
            estimates = listOf(
                EstimateEntity(
                    id = 1,
                    taskTitle = "Task A",
                    userName = "André",
                    finalAverage = 5.0f
                )
            )
        )

        composeTestRule.setContent {
            PointItTheme {
                HistoryScreen(
                    state = state,
                    onClearHistory = { clearClicked = true },
                    onBackClick = {},
                    onItemClick = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Limpar Histórico").performClick()

        assert(clearClicked)
    }
}