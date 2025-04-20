package com.example.agilizaapp.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agilizaapp.model.PedidoTemporal
import com.example.agilizaapp.ui.viewmodels.AnadirPedidoConProductosViewModel
import com.example.agilizaapp.ui.viewmodels.ProductoPreviewViewModel

@Composable
fun AnadirPedidoConProductos(
    codigo: String,
    pedidoTemporal: PedidoTemporal,
    modifier: Modifier = Modifier,
    productoVM: ProductoPreviewViewModel = viewModel(),
    pedidoVM: AnadirPedidoConProductosViewModel = viewModel()
) {
    val productos by productoVM.productos.collectAsState()
    val seleccionados by pedidoVM.seleccionados.collectAsState()
    val total by pedidoVM.total.collectAsState()
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = codigo,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Productos disponibles", fontWeight = FontWeight.Bold)
            LazyColumn(modifier = Modifier.height(150.dp)) {
                items(productos) { producto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${producto.codigo} - ${producto.nombre}")
                        IconButton(onClick = { pedidoVM.agregarProducto(producto) }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar producto")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))

            Text("Productos seleccionados", fontWeight = FontWeight.Bold)
            LazyColumn(modifier = Modifier.height(150.dp)) {
                items(seleccionados) { producto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${producto.codigo} - ${producto.nombre} - $${producto.valorVenta}")
                        }
                        IconButton(onClick = { pedidoVM.eliminarProducto(producto.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar producto")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text("Total: $${"%.2f".format(total)}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    pedidoVM.guardarPedidoCompleto(
                        codigo = codigo,
                        pedidoTemporal = pedidoTemporal,
                        total = total,
                        productos = seleccionados,
                        onSuccess = {
                            Toast.makeText(context, "Pedido guardado con éxito", Toast.LENGTH_SHORT).show()
                            pedidoVM.limpiarSeleccionados()
                        },
                        onError = {
                            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Pedido")
            }
        }
    }
}