package com.example.agilizaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.agilizaapp.ui.components.SearchBar
import com.example.agilizaapp.ui.components.TopBar
import com.example.agilizaapp.ui.components.BottomNavBar
import com.example.agilizaapp.ui.components.PedidoCard
import com.example.agilizaapp.ui.components.Producto
import com.example.agilizaapp.ui.theme.AgilizaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgilizaAppTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopBar(
                onMenuClick = { /* TODO: Abrir menú lateral */ },
                onProfileClick = { /* TODO: Ir al perfil del usuario */ }
            )
        },
        bottomBar = { BottomNavBar() },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            SearchBar()

            // 🔹 Agregar un pedido de prueba
            PedidoCard(
                codigo = "A10",
                fecha = "11 Febrero",
                hora = "10:30 am",
                estado = "Listo",
                cliente = "Mateo",
                barrio = "Santa Clara",
                domicilio = "$7.000",
                productos = listOf(
                    Producto("01", "Listo", "$40.000"),
                    Producto("05", "Listo", "$42.000")
                ),
                total = "$89.000",
                backgroundColor = Color(0xFFD0F0FF) // Azul claro
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AgilizaAppTheme {
        HomeScreen()
    }
}
