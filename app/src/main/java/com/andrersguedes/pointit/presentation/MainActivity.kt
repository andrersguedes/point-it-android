package com.andrersguedes.pointit.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andrersguedes.pointit.data.local.PointItDatabase
import com.andrersguedes.pointit.data.repository.EstimateRepository
import com.andrersguedes.pointit.presentation.history.HistoryScreen
import com.andrersguedes.pointit.presentation.history.HistoryViewModel
import com.andrersguedes.pointit.presentation.history.HistoryViewModelFactory
import com.andrersguedes.pointit.presentation.home.HomeScreen
import com.andrersguedes.pointit.presentation.home.HomeViewModel
import com.andrersguedes.pointit.presentation.room.RoomScreen
import com.andrersguedes.pointit.presentation.room.RoomViewModel
import com.andrersguedes.pointit.presentation.room.RoomViewModelFactory
import com.andrersguedes.pointit.presentation.theme.PointItTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Instala a API oficial de Splash Screen antes do super.onCreate
        installSplashScreen()

        // Ativa o modo de tela cheia edge-to-edge nativo
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            PointItTheme {
                // Aqui entrara o seu NavHost / Telas do App
                val navController = rememberNavController()

                // NavHost gerencia as telas do app
                NavHost(
                    navController = navController,
                    startDestination = "home_screen"
                ) {
                    // Rota 1: Tela Inicial (Home)
                    composable("home_screen") {
                        val viewModel: HomeViewModel = viewModel()
                        val state by viewModel.uiState.collectAsState()

                        HomeScreen(
                            state = state,
                            onUserNameChanged = viewModel::onUserNameChanged,
                            onTaskTitleChanged = viewModel::onTaskTitleChanged,
                            onSubmit = {
                                viewModel.validateAndProceed { name, task ->
                                    navController.navigate("room_screen/$name/$task")
                                }
                            },
                            onNavigateToHistory = {
                                navController.navigate("history_screen")
                            }
                        )
                    }

                    // Rota 2: Mesa de Votação (Planning Poker)
                    composable(
                        route = "room_screen/{userName}/{taskTitle}",
                        arguments = listOf(
                            navArgument("userName") { type = NavType.StringType },
                            navArgument("taskTitle") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val context = LocalContext.current
                        val database = PointItDatabase.getDatabase(context)
                        val repository = EstimateRepository(database.estimateDao())
                        val factory = RoomViewModelFactory(repository)

                        val userName = backStackEntry.arguments?.getString("userName") ?: ""
                        val taskTitle = backStackEntry.arguments?.getString("taskTitle") ?: ""

                        val roomViewModel: RoomViewModel = viewModel(factory = factory)
                        val state by roomViewModel.uiState.collectAsState()

                        // Inicializa a sessão assim que entra na tela
                        LaunchedEffect(userName, taskTitle) {
                            roomViewModel.initSession(userName, taskTitle)
                        }

                        RoomScreen(
                            state = state,
                            onVoteSelected = roomViewModel::selectVote,
                            onRevealVotes = roomViewModel::revealVotes,
                            onResetVoting = roomViewModel::resetVoting,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // Rota 3: Histórico de Estimativas
                    composable("history_screen") {
                        val context = LocalContext.current
                        val database = PointItDatabase.getDatabase(context)
                        val repository = EstimateRepository(database.estimateDao())
                        val factory = HistoryViewModelFactory(repository)

                        val historyViewModel: HistoryViewModel = viewModel(factory = factory)
                        val state by historyViewModel.uiState.collectAsState()

                        HistoryScreen(
                            state = state,
                            onClearHistory = historyViewModel::clearHistory,
                            onBackClick = { navController.popBackStack() },
                            onItemClick = { estimateId ->
                                // Aqui entra a navegação para a Tela 4 de Detalhes futuramente!
                            }
                        )
                    }
                }
            }
        }
    }

}