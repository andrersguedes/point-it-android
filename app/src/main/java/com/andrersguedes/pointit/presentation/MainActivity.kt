package com.andrersguedes.pointit.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andrersguedes.pointit.presentation.home.HomeScreen
import com.andrersguedes.pointit.presentation.home.HomeViewModel
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
                }
            }
        }
    }

}