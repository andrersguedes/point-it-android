package com.andrersguedes.pointit.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.andrersguedes.pointit.R
import com.andrersguedes.pointit.presentation.theme.PointItTheme
import com.andrersguedes.pointit.presentation.theme.Spacing

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToRoom: (userName: String, taskTitle: String) -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Cria o estado de scroll
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(scrollState)
                .padding(Spacing.large),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo / Cabeçalho
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            Text(
                text = stringResource(R.string.home_screen_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.xxl))

            // Card container para os inputs
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Spacing.medium),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = Spacing.xxs)
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.large),
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium)
                ) {
                    // Campo: Nome do Usuário
                    OutlinedTextField(
                        value = state.userName,
                        onValueChange = { viewModel.onUserNameChanged(it) },
                        label = { Text(stringResource(R.string.home_screen_text_your_name)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(Spacing.medium)
                    )

                    // Campo: Título da Tarefa
                    OutlinedTextField(
                        value = state.taskTitle,
                        onValueChange = { viewModel.onTaskTitleChanged(it) },
                        label = { Text(stringResource(R.string.home_screen_text_your_task)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Task, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(Spacing.medium)
                    )

                    // Exibição de Erro caso exista
                    if (state.errorType == HomeError.EMPTY_FIELDS) {
                        Text(
                            text = stringResource(R.string.home_screen_error_message),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(Spacing.small))

                    // Botão Principal: Criar / Entrar na Sala
                    Button(
                        onClick = {
                            viewModel.validateAndProceed { name, task ->
                                onNavigateToRoom(name, task)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Spacing.xxl),
                        shape = RoundedCornerShape(Spacing.medium)
                    ) {
                        Text(
                            text = stringResource(R.string.home_screen_button_text),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.large))

            // Botão Secundário: Histórico
            TextButton(onClick = onNavigateToHistory) {
                Text(
                    text = stringResource(R.string.home_screen_history_button_text),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    PointItTheme {
        HomeScreen(
            viewModel = HomeViewModel(),
            onNavigateToRoom = { _, _ -> },
            onNavigateToHistory = { }
        )
    }
}