package com.example.agilizaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.example.agilizaapp.ui.components.SearchBar
import com.example.agilizaapp.ui.components.TarjetaPedido

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agilizaapp.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val pedidos = viewModel.pedidos.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        SearchBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pedidos) { pedido ->
                TarjetaPedido(
                    codigo = pedido.codigo,
                    fecha = pedido.fecha,
                    hora = pedido.hora,
                    cliente = pedido.nombreCliente,
                    barrio = pedido.barrio,
                    domicilio = "$${pedido.valorDomicilio}",
                    productosIniciales = listOf(),
                    total = "$${pedido.valorTotal}",
                    backgroundColor = Color(0xFFD0F0FF)
                )
            }
        }
    }
}
