package com.andrersguedes.pointit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrersguedes.pointit.presentation.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PointItEndToEndTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeScreenWhenFieldsEmptyShouldntNavigateAndShowError() {
        with(composeTestRule) {
            // Clicar direto sem preencher
            onNodeWithText("Criar Sala de Votação").performClick()

            // Validar se o erro aparece e se continua na Home
            onNodeWithText("Por favor, preencha seu nome e o título da task.").assertIsDisplayed()
            onNodeWithText("Point It").assertIsDisplayed()
        }
    }

    @Test
    fun completeFlowHomeToRoomScreenCreationToVote() {
        with(composeTestRule) {
            // Preencher formulário
            onNodeWithText("Seu Nome").performTextInput("André")
            onNodeWithText("História / Task (Ex: [PROJ-102])").performTextInput("[PROJ-999] Teste E2E")

            // Avançar para a sala
            onNodeWithText("Criar Sala de Votação").performClick()

            // Validar se navegou e os dados estão corretos
            onNodeWithText("[PROJ-999] Teste E2E").assertIsDisplayed()
            onNodeWithText("Sala ativa • Participante: André").assertIsDisplayed()

            // Interagir com o deck (Votar 8)
            onNodeWithText("8").performClick()

            // Revelar votos e validar a média
            onNodeWithText("Revelar Votos").performClick()
            onNodeWithText("Média de Estimativa: 5.8").assertIsDisplayed()
        }
    }

    @Test
    fun roomScreenWhenUserChangeVoteBeforeRevealShouldCalculateAverageWithLastVote() {
        with(composeTestRule) {
            // Ir para a Room
            onNodeWithText("Seu Nome").performTextInput("André")
            onNodeWithText("História / Task (Ex: [PROJ-102])").performTextInput("[PROJ-102] Teste Troca Voto")
            onNodeWithText("Criar Sala de Votação").performClick()

            // Vota 3, depois muda para 13
            onNodeWithText("3").performClick()
            onNodeWithText("13").performClick()

            onNodeWithText("Revelar Votos").performClick()

            onNodeWithText("Média de Estimativa: 6.8").assertIsDisplayed()
        }
    }

    @Test
    fun roomScreenWhenResetVoteShouldClearResultAndAllowVoteAgain() {
        with(composeTestRule) {
            // Navegar e fazer primeira votação
            onNodeWithText("Seu Nome").performTextInput("André")
            onNodeWithText("História / Task (Ex: [PROJ-102])").performTextInput("[PROJ-103] Reset Test")
            onNodeWithText("Criar Sala de Votação").performClick()

            onNodeWithText("8").performClick()
            onNodeWithText("Revelar Votos").performClick()

            // Clicar em Resetar
            onNodeWithText("Nova Votação (Resetar)").performClick()

            // Validar que o resultado sumiu e o botão voltou ao estado normal
            onNodeWithText("Média de Estimativa: 7.0").assertDoesNotExist()
            onNodeWithText("Revelar Votos").assertIsDisplayed()
        }
    }

    @Test
    fun roomScreenWhenClickOnBackButtonShouldReturnToHomeScreen() {
        with(composeTestRule) {
            // Preencher formulário e navegar para a RoomScreen
            onNodeWithText("Seu Nome").performTextInput("André")
            onNodeWithText("História / Task (Ex: [PROJ-102])").performTextInput("[PROJ-104] Teste Botao Voltar")
            onNodeWithText("Criar Sala de Votação").performClick()

            // Validar que estamos na RoomScreen
            onNodeWithText("[PROJ-104] Teste Botao Voltar").assertIsDisplayed()

            // Clicar no botão de voltar (ícone de seta na TopAppBar)
            onNodeWithContentDescription("Voltar para Home").performClick()

            // Validar que retornou para a HomeScreen verificando o título ou botão inicial
            onNodeWithText("Point It").assertIsDisplayed()
            onNodeWithText("Criar Sala de Votação").assertIsDisplayed()
        }
    }

}