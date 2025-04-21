package com.example.agilizaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.agilizaapp.ui.components.ProductCard
import com.example.agilizaapp.ui.viewmodels.ProductoViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProductGrid(
    onClickAnadirProducto: () -> Unit,
    productoViewModel: ProductoViewModel = viewModel()
) {
    val productos by productoViewModel.productos.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    // Recarga de datos
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            productoViewModel.cargarProductos()
            kotlinx.coroutines.delay(1000)
            isRefreshing = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { isRefreshing = true }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp, bottom = 80.dp, start = 8.dp, end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos) { producto ->
                        ProductCard(
                            number = producto.codigo.toString(),
                            title = producto.nombre,
                            price = producto.valorVenta.toString(),
                            imageUri = producto.fotoUriLocal,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onClickAnadirProducto,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Añadir producto")
        }
    }
}
