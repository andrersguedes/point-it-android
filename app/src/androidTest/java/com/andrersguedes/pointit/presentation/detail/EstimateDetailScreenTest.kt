package com.andrersguedes.pointit.presentation.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import com.andrersguedes.pointit.presentation.theme.PointItTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EstimateDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenStateIsLoadingShouldShowLoadingMessage() {
        val state = EstimateDetailUiState(isLoading = true)

        composeTestRule.setContent {
            PointItTheme {
                EstimateDetailScreen(state = state, onBackClick = {})
            }
        }

        composeTestRule.onNodeWithText("Carregando detalhes...").assertIsDisplayed()
    }

    @Test
    fun whenEstimateIsNullShouldShowErrorMessage() {
        val state = EstimateDetailUiState(isLoading = false, estimate = null)

        composeTestRule.setContent {
            PointItTheme {
                EstimateDetailScreen(state = state, onBackClick = {})
            }
        }

        composeTestRule.onNodeWithText("Estimativa não encontrada.").assertIsDisplayed()
    }

    @Test
    fun whenHaveValidEstimateShouldShowAllDetails() {
        val estimate = EstimateEntity(
            id = 42L,
            taskTitle = "[PROJ-777] Teste Tela Detalhes UI",
            userName = "André",
            finalAverage = 13.0f,
            timestamp = 1718000000000L // Data fixa para teste
        )
        val state = EstimateDetailUiState(isLoading = false, estimate = estimate)

        composeTestRule.setContent {
            PointItTheme {
                EstimateDetailScreen(state = state, onBackClick = {})
            }
        }

        composeTestRule.onNodeWithText("[PROJ-777] Teste Tela Detalhes UI").assertIsDisplayed()
        composeTestRule.onNodeWithText("13.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("André").assertIsDisplayed()
    }

    @Test
    fun whenClickOnBackButtonShouldCallOnBackClick() {
        var backClicked = false
        val state = EstimateDetailUiState(
            isLoading = false,
            estimate = EstimateEntity(id = 1L, taskTitle = "Task", userName = "User", finalAverage = 5.0f)
        )

        composeTestRule.setContent {
            PointItTheme {
                EstimateDetailScreen(state = state, onBackClick = { backClicked = true })
            }
        }

        composeTestRule.onNodeWithContentDescription("Voltar").performClick()

        assertTrue(backClicked)
    }
}