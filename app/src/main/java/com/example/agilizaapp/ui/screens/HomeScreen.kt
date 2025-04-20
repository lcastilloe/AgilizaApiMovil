package com.example.agilizaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.example.agilizaapp.ui.components.SearchBar
import com.example.agilizaapp.ui.components.TarjetaPedido
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agilizaapp.ui.viewmodels.HomeViewModel
fun <T> List<T>.chunkedPairs(): List<List<T>> {
    return this.chunked(2)
}

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val pedidos = viewModel.pedidos.collectAsState().value
    var isRefreshing by remember { mutableStateOf(false) }

    // Este bloque se ejecutará cuando isRefreshing cambie a true
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.cargarPedidos()
            kotlinx.coroutines.delay(1500)
            isRefreshing = false
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        SearchBar()
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
            }
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Agrupar los pedidos de dos en dos para mostrarlos por fila
                items(pedidos.chunkedPairs()) { parPedidos ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (pedido in parPedidos) {
                            TarjetaPedido(
                                modifier = Modifier
                                    .weight(1f), // Para que las tarjetas ocupen el mismo espacio
                                codigo = pedido.codigo,
                                fecha = pedido.fecha,
                                hora = pedido.hora,
                                cliente = pedido.nombreCliente,
                                barrio = pedido.barrio,
                                domicilio = "$${pedido.valorDomicilio}",
                                productosIniciales = pedido.productos,
                                total = "$${pedido.valorTotal}",
                                backgroundColor = Color(0xFFD0F0FF)
                            )
                        }

                        // Si hay solo un pedido en esta fila, agregamos un Spacer para balancear
                        if (parPedidos.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

