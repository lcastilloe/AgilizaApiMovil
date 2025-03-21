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
import com.example.agilizaapp.ui.components.TarjetaPedido
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

            // 🔹 Mostrar dos tarjetas lado a lado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TarjetaPedido(
                    codigo = "A10",
                    fecha = "11 Febrero",
                    hora = "10:30 am",
                    cliente = "Mateo",
                    barrio = "Santa Clara",
                    domicilio = "$7.000",
                    productosIniciales = listOf(
                        Producto("01", "Inicio", "$40.000"),
                        Producto("02", "Preparación", "$50.000"),
                        Producto("03", "Listo", "$60.000")
                    ),
                    total = "$150.000",
                    backgroundColor = Color(0xFFD0F0FF)
                )
                TarjetaPedido(
                    codigo = "A10",
                    fecha = "11 Febrero",
                    hora = "10:30 am",
                    cliente = "Mateo",
                    barrio = "Santa Clara",
                    domicilio = "$7.000",
                    productosIniciales = listOf(
                        Producto("01", "Inicio", "$40.000"),
                        Producto("02", "Preparación", "$50.000"),
                        Producto("03", "Listo", "$60.000")
                    ),
                    total = "$150.000",
                    backgroundColor = Color(0xFFD0F0FF)
                )
            }
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