package com.example.agilizaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.agilizaapp.ui.components.AnadirPedido
import com.example.agilizaapp.ui.components.BottomNavBar
import com.example.agilizaapp.ui.components.TopBar
import com.example.agilizaapp.ui.navigation.Screen
import com.example.agilizaapp.ui.screens.HomeScreen
import com.example.agilizaapp.ui.screens.LoginScreen
import com.example.agilizaapp.ui.screens.ProductGrid
import com.example.agilizaapp.ui.theme.AgilizaAppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AgilizaAppTheme {
                val currentUser = FirebaseAuth.getInstance().currentUser
                var currentScreen by remember {
                    mutableStateOf(
                        if (currentUser == null) Screen.LOGIN else Screen.PEDIDOS
                    )
                }

                Scaffold(
                    topBar = {
                        if (currentScreen != Screen.LOGIN) {
                            TopBar(
                                onMenuClick = { /* TODO: Menú lateral */ },
                                onProfileClick = { /* TODO: Perfil */ }
                            )
                        }
                    },
                    bottomBar = {
                        if (currentScreen != Screen.LOGIN) {
                            BottomNavBar(
                                selectedScreen = currentScreen,
                                onItemSelected = { screen ->
                                    currentScreen = screen
                                }
                            )
                        }
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        when (currentScreen) {
                            Screen.LOGIN -> LoginScreen(
                                onLoginSuccess = {
                                    currentScreen = Screen.PEDIDOS
                                }
                            )
                            Screen.PRODUCTOS -> ProductGrid()
                            Screen.PEDIDOS -> HomeScreen()
                            Screen.ANADIR_PEDIDO -> AnadirPedido()
                            else -> HomeScreen()
                        }
                    }
                }
            }
        }
    }
}
