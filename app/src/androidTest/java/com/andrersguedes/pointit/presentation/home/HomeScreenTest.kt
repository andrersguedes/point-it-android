package com.andrersguedes.pointit.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    @Test
    fun whenScreenLoadShouldShowTitleInputsAndButtonsCorrectly() {
        with(composeTestRule) {
            setContent {
                PointItTheme {
                    HomeScreen(
                        state = HomeUiState(),
                        onUserNameChanged = {},
                        onTaskTitleChanged = {},
                        onSubmit = {},
                        onNavigateToHistory = {}
                    )
                }
            }

            onNodeWithText("Point It").assertIsDisplayed()
            onNodeWithText("Planejamento de sprints ágeis sem atritos")
                .assertIsDisplayed()
            onNodeWithText("Seu Nome").assertIsDisplayed()
            onNodeWithText("História / Task (Ex: [PROJ-102])").assertIsDisplayed()
            onNodeWithText("Criar Sala de Votação").assertIsDisplayed()
            onNodeWithText("Ver Histórico de Estimativas").assertIsDisplayed()
        }
    }

    @Test
    fun whenUserClickOnBuildRoomWithoutFillShouldShowErrorMessage() {
        with(composeTestRule) {
            setContent {
                // Simulamos o estado reativo localmente no teste
                var uiState by remember { mutableStateOf(HomeUiState()) }

                PointItTheme {
                    HomeScreen(
                        state = uiState,
                        onUserNameChanged = {},
                        onTaskTitleChanged = {},
                        onSubmit = {
                            // Ao clicar, simulamos a regra que o ViewModel faria: emitir o erro
                            uiState = uiState.copy(errorType = HomeError.EMPTY_FIELDS)
                        },
                        onNavigateToHistory = {}
                    )
                }
            }

            onNodeWithText("Criar Sala de Votação").performClick()
            onNodeWithText("Por favor, preencha seu nome e o título da task.")
                .assertIsDisplayed()
        }
    }

}