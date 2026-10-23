package com.andrersguedes.pointit.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andrersguedes.pointit.presentation.home.HomeScreen
import com.andrersguedes.pointit.presentation.home.HomeViewModel
import com.andrersguedes.pointit.presentation.theme.PointItTheme

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

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
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToRoom = { userName, taskTitle ->
                                // Aqui faremos a navegação para a Tela 2 no futuro
                                // Ex: navController.navigate("room_screen/$userName/$taskTitle")
                            },
                            onNavigateToHistory = {
                                // Aqui faremos a navegação para a Tela de Histórico
                                // Ex: navController.navigate("history_screen")
                            }
                        )
                    }
                }
            }
        }
    }

}