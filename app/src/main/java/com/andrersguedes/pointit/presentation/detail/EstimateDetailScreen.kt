package com.andrersguedes.pointit.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.andrersguedes.pointit.R
import com.andrersguedes.pointit.presentation.theme.PointItTheme
import com.andrersguedes.pointit.presentation.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimateDetailScreen(
    state: EstimateDetailUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.estimate_detail_text_details)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.estimate_detail_button_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.estimate_detail_text_loading))
            }
        } else if (state.estimate == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.estimate_detail_text_not_found))
            }
        } else {
            val estimate = state.estimate
            val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", LocalLocale.current.platformLocale)
                .format(Date(estimate.timestamp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(Spacing.medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.medium)
            ) {
                // Card Principal de Resumo
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.medium),
                        verticalArrangement = Arrangement.spacedBy(Spacing.small)
                    ) {
                        Text(
                            text = stringResource(R.string.estimate_detail_text_history_task),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = estimate.taskTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(Spacing.extraSmall))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = stringResource(R.string.estimate_detail_text_final_average), style = MaterialTheme.typography.labelMedium)
                                Text(
                                    text = "%.1f".format(estimate.finalAverage),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = stringResource(R.string.estimate_detail_text_moderator), style = MaterialTheme.typography.labelMedium)
                                Text(
                                    text = estimate.userName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(Spacing.extraSmall))
                        Text(
                            text = stringResource(R.string.estimate_detail_text_conclusion, formattedDate),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RoomScreenPreview() {
    PointItTheme {
        EstimateDetailScreen(
            state = EstimateDetailUiState(),
            onBackClick = {}
        )
    }
}