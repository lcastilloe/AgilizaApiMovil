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
//import com.example.agilizaapp.ui.screens.AgendaScreen
import com.example.agilizaapp.ui.screens.HomeScreen

import com.example.agilizaapp.ui.screens.ProductGrid
//import com.example.agilizaapp.ui.screens.ProductosScreen
import com.example.agilizaapp.ui.theme.AgilizaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgilizaAppTheme {
                var currentScreen by remember { mutableStateOf(Screen.PEDIDOS) }

                Scaffold(
                    topBar = {
                        TopBar(
                            onMenuClick = { /* TODO: Menú lateral */ },
                            onProfileClick = { /* TODO: Perfil */ }
                        )
                    },
                    bottomBar = {
                        BottomNavBar(
                            selectedScreen = currentScreen,
                            onItemSelected = { screen ->
                                currentScreen = screen
                            }
                        )
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                    ) {
                        when (currentScreen) {
                            Screen.PRODUCTOS -> ProductGrid()
                            Screen.PEDIDOS -> HomeScreen()
                            Screen.ANADIR_PEDIDO -> AnadirPedido()
                            //Screen.CONTABILIDAD -> PantallaConFechaYHora1()
                            else -> HomeScreen()
                        }
                    }
                }
            }
        }
    }
}
