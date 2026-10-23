package com.andrersguedes.pointit.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.andrersguedes.pointit.presentation.theme.PointItTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Instala a API oficial de Splash Screen antes do super.onCreate
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            PointItTheme {
                // Aqui entrara o seu NavHost / Telas do App
            }
        }
    }

}