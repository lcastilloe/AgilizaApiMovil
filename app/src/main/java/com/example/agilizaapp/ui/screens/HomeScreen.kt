package com.example.agilizaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.agilizaapp.ui.Producto
import com.example.agilizaapp.ui.components.SearchBar
import com.example.agilizaapp.ui.components.TarjetaPedido


@Composable
fun HomeScreen() {

        Column(
            modifier = Modifier
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