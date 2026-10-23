package com.andrersguedes.pointit.presentation.room

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
class RoomScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenScreenLoadShouldShowTaskAndParticipantsCorrectly() {
        val initialState = RoomUiState(
            userName = "André",
            taskTitle = "[PROJ-102] Refatorar SDK",
            participants = listOf(
                Participant(id = "1", name = "André", hasVoted = false),
                Participant(id = "2", name = "Ana Tech Lead", hasVoted = true, selectedVote = "5")
            )
        )

        with(composeTestRule) {
            setContent {
                PointItTheme {
                    RoomScreen(
                        state = initialState,
                        onVoteSelected = {},
                        onRevealVotes = {},
                        onResetVoting = {},
                        onBackClick = {}
                    )
                }
            }

            onNodeWithText("[PROJ-102] Refatorar SDK").assertIsDisplayed()
            onNodeWithText("André").assertIsDisplayed()
            onNodeWithText("Ana Tech Lead").assertIsDisplayed()
            onNodeWithText("Escolha sua carta:").assertIsDisplayed()
            onNodeWithText("Revelar Votos").assertIsDisplayed()
        }
    }

    @Test
    fun whenVotesRevealShouldShowAverageValue() {
        val revealedState = RoomUiState(
            userName = "André",
            taskTitle = "[PROJ-102] Refatorar SDK",
            participants = listOf(
                Participant(id = "1", name = "André", hasVoted = true, selectedVote = "5"),
                Participant(id = "2", name = "Ana Tech Lead", hasVoted = true, selectedVote = "5")
            ),
            areVotesRevealed = true,
            averageScore = 5.0f
        )

        with(composeTestRule) {
            setContent {
                PointItTheme {
                    RoomScreen(
                        state = revealedState,
                        onVoteSelected = {},
                        onRevealVotes = {},
                        onResetVoting = {},
                        onBackClick = {}
                    )
                }
            }

            onNodeWithText("Média de Estimativa: 5.0").assertIsDisplayed()
            onNodeWithText("Nova Votação (Resetar)").assertIsDisplayed()
        }
    }

    @Test
    fun whenUserClickOnFibonacciCardShouldCallOnVoteSelectedWithSelectedValue() {
        var selectedVoteCaptured: String? = null
        val initialState = RoomUiState(
            userName = "André",
            taskTitle = "[PROJ-102] Refatorar SDK",
            participants = listOf(
                Participant(id = "1", name = "André", hasVoted = false)
            )
        )

        with(composeTestRule) {
            setContent {
                PointItTheme {
                    RoomScreen(
                        state = initialState,
                        onVoteSelected = { vote -> selectedVoteCaptured = vote },
                        onRevealVotes = {},
                        onResetVoting = {},
                        onBackClick = {}
                    )
                }
            }

            onNodeWithText("8").performClick()
        }

        assert(selectedVoteCaptured == "8")
    }

}