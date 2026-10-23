package com.andrersguedes.pointit.presentation.home

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrersguedes.pointit.presentation.theme.PointItTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @SuppressLint("ViewModelConstructorInComposable")
    @Test
    fun whenScreenLoadShouldShowTitleInputsAndButtonsCorrectly() {
        composeTestRule.setContent {
            PointItTheme {
                HomeScreen(
                    viewModel = HomeViewModel(),
                    onNavigateToRoom = { _, _ -> },
                    onNavigateToHistory = { }
                )
            }
        }

        composeTestRule.onNodeWithText("Point It").assertIsDisplayed()
        composeTestRule.onNodeWithText("Planejamento de sprints ágeis sem atritos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Seu Nome").assertIsDisplayed()
        composeTestRule.onNodeWithText("História / Task (Ex: [PROJ-102])").assertIsDisplayed()
        composeTestRule.onNodeWithText("Criar Sala de Votação").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ver Histórico de Estimativas").assertIsDisplayed()
    }

    @SuppressLint("ViewModelConstructorInComposable")
    @Test
    fun whenUserClickOnBuildRoomWithoutFillShouldShowErrorMessage() {
        composeTestRule.setContent {
            PointItTheme {
                HomeScreen(
                    viewModel = HomeViewModel(),
                    onNavigateToRoom = { _, _ -> },
                    onNavigateToHistory = { }
                )
            }
        }

        composeTestRule.onNodeWithText("Criar Sala de Votação").performClick()
        composeTestRule.onNodeWithText("Por favor, preencha seu nome e o título da task.").assertIsDisplayed()
    }

}