package com.example.agilizaapp.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agilizaapp.ui.components.formatearFecha
import com.example.agilizaapp.ui.data.ProductoEstadoPedido
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AgendaScreen() {
    var pedidos by remember { mutableStateOf<List<PedidoAgenda>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    val context = LocalContext.current

    // Cargar pedidos desde Firestore
    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val querySnapshot = db.collection("usuarios")
                    .document(uid)
                    .collection("pedidos")
                    .get()
                    .await()

                val pedidosList = mutableListOf<PedidoAgenda>()

                for (document in querySnapshot.documents) {
                    val productosRef = document.reference.collection("productos").get().await()
                    val productos = productosRef.documents.map { productoDoc ->
                        ProductoEstadoPedido(
                            codigo = productoDoc.getString("codigo") ?: "",
                            estado = productoDoc.getString("estado") ?: "Inicio",
                            precio = "$${productoDoc.getDouble("valorVenta")?.toInt() ?: 0}"
                        )
                    }

                    val pedido = PedidoAgenda(
                        codigo = document.getString("codigo") ?: "",
                        fecha = document.getString("fecha") ?: "",
                        hora = document.getString("hora") ?: "",
                        nombreCliente = document.getString("nombreCliente") ?: "",
                        direccionEntrega = document.getString("direccionEntrega") ?: "",
                        barrioEntrega = document.getString("barrioEntrega") ?: "",
                        productos = productos
                    )
                    pedidosList.add(pedido)
                }

                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val hoy = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time

                val pedidosFiltrados = pedidosList.filter { pedido ->
                    try {
                        val fechaPedido = formato.parse(pedido.fecha)
                        fechaPedido != null && !fechaPedido.before(hoy)
                    } catch (e: Exception) {
                        false
                    }
                }.sortedWith(compareBy({ it.fecha }, { it.hora }))

                pedidos = pedidosFiltrados
                isLoading = false

                // ⏰ Programar notificaciones
                programarNotificaciones(context, pedidosFiltrados)

            } catch (e: Exception) {
                println("Error cargando pedidos: ${e.message}")
                isLoading = false
            }
        }
    }

    // UI
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            val pedidosAgrupados = pedidos.groupBy { it.fecha }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pedidosAgrupados.forEach { (fecha, pedidosPorFecha) ->
                    item {
                        Text(
                            text = formatearFecha(fecha),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        )
                    }

                    items(pedidosPorFecha) { pedido ->
                        TarjetaAgenda(pedido = pedido)
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaAgenda(
    pedido: PedidoAgenda,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Pedido: ${pedido.codigo}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Cliente: ${pedido.nombreCliente}",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Hora: ${pedido.hora}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Modelo de datos para la agenda
data class PedidoAgenda(
    val codigo: String,
    val fecha: String,
    val hora: String,
    val nombreCliente: String,
    val direccionEntrega: String,
    val barrioEntrega: String,
    val productos: List<ProductoEstadoPedido>
)