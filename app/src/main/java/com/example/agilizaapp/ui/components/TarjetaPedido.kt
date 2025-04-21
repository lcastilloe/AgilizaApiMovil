package com.example.agilizaapp.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agilizaapp.ui.data.ProductoEstadoPedido
import com.example.agilizaapp.ui.theme.AgilizaAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

@Composable
fun TarjetaPedido(
    codigo: String,
    fecha: String,
    hora: String,
    cliente: String,
    barrio: String,
    domicilio: String,
    productosIniciales: List<ProductoEstadoPedido>,
    total: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    // Asegurar que todos los productos comiencen en "Inicio"
    val productos = remember {
        mutableStateListOf(*productosIniciales.toTypedArray())
    }


    // Estado mutable para el estado del pedido
    var estadoPedido by remember { mutableStateOf(calcularEstadoPedido(productos.map { it.estado })) }
    val estadoColorPedido = when (estadoPedido) {
        "Listo" -> MaterialTheme.colorScheme.primary
        "Preparación" -> MaterialTheme.colorScheme.tertiary
        "Inicio" -> MaterialTheme.colorScheme.onErrorContainer
        else -> Color.Gray
    }
    val estadoColorPedidoTarjetaFuerte = when (estadoPedido) {
        "Listo" -> MaterialTheme.colorScheme.inversePrimary
        "Preparación" -> MaterialTheme.colorScheme.tertiaryContainer
        "Inicio" -> MaterialTheme.colorScheme.error
        else -> Color.Gray
    }
    val estadoColorPedidoTarjetaClaro = when (estadoPedido) {
        "Listo" -> MaterialTheme.colorScheme.primaryContainer
        "Preparación" -> MaterialTheme.colorScheme.surfaceContainer
        "Inicio" -> MaterialTheme.colorScheme.errorContainer
        else -> Color.Gray
    }
    val colorTextoFranja = when (estadoPedido) {
        "Inicio" -> Color.White
        "Preparación" -> MaterialTheme.colorScheme.onTertiaryContainer
        "Listo" -> MaterialTheme.colorScheme.primary
        else -> Color.Gray
    }


    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = estadoColorPedidoTarjetaClaro),
        modifier = modifier
            .padding(8.dp)
            .width(170.dp)
            .height(230.dp)
    ) {
        Column {
            // Título con código y fecha
            Row(
                modifier = Modifier
                    .background(estadoColorPedidoTarjetaFuerte)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$codigo - ${formatearFecha(fecha)}",
                    color = colorTextoFranja,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Estado del pedido y hora
            Row(
                modifier = Modifier.fillMaxWidth().height(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {




                    Box(
                        modifier = Modifier
                            .background(color = estadoColorPedido, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = estadoPedido,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                }

                Text(
                    text = formatearHora(hora),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp, // ← reducido de 16.sp a 12.sp
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )

            }

            // Tabla de encabezados

            FilaTabla(
                columnas = listOf("Cliente", "Barrio", "Domicilio"),
                backgroundColor = estadoColorPedidoTarjetaFuerte,
                negrita = true // Cambia a false si quieres el texto normal
            )
            FilaTabla(
                columnas = listOf(cliente, barrio, domicilio),
                backgroundColor = estadoColorPedidoTarjetaClaro,
                negrita = false // Cambia a false si quieres el texto normal
            )
            FilaTabla(
                columnas = listOf("Producto", "Estado", "Precio"),
                backgroundColor = estadoColorPedidoTarjetaFuerte,
                negrita = true // Cambia a false si quieres el texto normal
            )

            // Tabla de productos con estados interactivos
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp) // Puedes ajustar esto según el espacio disponible
            ) {
                items(productos.size) { index ->
                    val producto = productos[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        FilaProducto(
                            elementos = listOf(
                                {
                                    Text(
                                        producto.codigo,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                {
                                    BoxEstado(
                                        estadoInicial = producto.estado
                                    ) { nuevoEstado ->
                                        productos[index] = productos[index].copy(estado = nuevoEstado)
                                        estadoPedido = calcularEstadoPedido(productos.map { it.estado })

                                        // ACTUALIZA EL ESTADO DEL PRODUCTO EN FIRESTORE
                                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                                        if (uid != null) {
                                            FirebaseFirestore.getInstance()
                                                .collection("usuarios")
                                                .document(uid)
                                                .collection("pedidos")
                                                .whereEqualTo("codigo", codigo)
                                                .get()
                                                .addOnSuccessListener { pedidosSnapshot ->
                                                    val pedidoDoc = pedidosSnapshot.documents.firstOrNull()
                                                    pedidoDoc?.reference
                                                        ?.collection("productos")
                                                        ?.whereEqualTo("codigo", producto.codigo)
                                                        ?.get()
                                                        ?.addOnSuccessListener { productosSnapshot ->
                                                            val productoDoc = productosSnapshot.documents.firstOrNull()
                                                            productoDoc?.reference?.update("estado", nuevoEstado)
                                                        }

                                                    // ACTUALIZA EL ESTADO DEL PEDIDO
                                                    val nuevoEstadoPedido = calcularEstadoPedido(productos.map { it.estado })
                                                    pedidoDoc?.reference?.update("estado", nuevoEstadoPedido)
                                                }
                                        }
                                    }

                                },
                                {
                                    Text(
                                        producto.precio,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            ),
                            backgroundColor = estadoColorPedidoTarjetaClaro,
                            negrita = false
                        )
                    }
                }
            }


            // Total
            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOTAL: $total",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun calcularEstadoPedido(estadosProductos: List<String>): String {
    return when {
        estadosProductos.all { it == "Listo" } -> "Listo"
        estadosProductos.any { it == "Listo" } -> "Preparación"
        estadosProductos.any { it == "Preparación" } -> "Preparación"
        else -> "Inicio"
    }
}

@Preview
@Composable
fun PreviewTarjetaPedido() {
    AgilizaAppTheme {
        TarjetaPedido(
            codigo = "A10",
            fecha = "11 Febrero",
            hora = "10:30 am",
            cliente = "Mateo",
            barrio = "Santa Clara",
            domicilio = "$7.000",
            productosIniciales = listOf(
                ProductoEstadoPedido("01", "Inicio", "$40.000"),
                ProductoEstadoPedido("02", "Preparación", "$50.000"),
                ProductoEstadoPedido("03", "Listo", "$60.000")
            ),
            total = "$150.000",
            backgroundColor = Color(0xFFD0F0FF)
        )
    }
}

@Composable
fun FilaTabla(
    columnas: List<String>,
    backgroundColor: Color,
    negrita: Boolean
) {
    val colorTexto = when (backgroundColor) {
        MaterialTheme.colorScheme.inversePrimary -> MaterialTheme.colorScheme.primary
        MaterialTheme.colorScheme.tertiaryContainer -> MaterialTheme.colorScheme.onTertiaryContainer
        MaterialTheme.colorScheme.error -> Color.White
        else -> MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        columnas.forEach { texto ->
            Text(
                text = texto,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp,
                color = colorTexto
            )
        }
    }
}


@Composable
fun FilaProducto(
    elementos: List<@Composable () -> Unit>, // Permite recibir cualquier composable en lugar de solo texto
    backgroundColor: Color, // Color de fondo de la fila
    negrita: Boolean // Indica si el texto debe ser negrita
) {
    Row(
        modifier = Modifier
            .background(backgroundColor) // Aplica el color de fondo
            .fillMaxWidth()
            .height(20.dp),
        //horizontalArrangement = Arrangement.Center, // Centra los elementos horizontalmente
        //verticalAlignment = Alignment.CenterVertically // Centra verticalmente los textos
    ) {
        elementos.forEach { elemento ->
            Box(
                modifier = Modifier.weight(1f), // Ocupa el mismo espacio en la fila
                contentAlignment = Alignment.Center
            ) {
                elemento() // Renderiza el composable correspondiente
            }
        }
    }
}

@Composable
fun BoxEstado(estadoInicial: String, onEstadoChange: (String) -> Unit) {
    var estado by remember { mutableStateOf(estadoInicial) }

    val estadoColor = when (estado) {
        "Listo" -> MaterialTheme.colorScheme.primary
        "Preparación" -> MaterialTheme.colorScheme.tertiary
        "Inicio" -> MaterialTheme.colorScheme.onErrorContainer
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .background(color = estadoColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 2.dp).fillMaxWidth()
            .clickable {
                estado = cambiarEstado(estado)
                onEstadoChange(estado) // Notifica el cambio de estado
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = estado,
            fontSize = 8.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}


// Función para cambiar el estado de un producto en orden
fun cambiarEstado(estadoActual: String): String {
    return when (estadoActual) {
        "Inicio" -> "Preparación"
        "Preparación" -> "Listo"
        "Listo" -> "Inicio"
        else -> "Inicio"
    }
}
fun formatearHora(hora: String): String {
    return try {
        val formatoEntrada = java.text.SimpleDateFormat("HH:mm", Locale("es", "ES"))
        val formatoSalida = java.text.SimpleDateFormat("hh:mm a", Locale("es", "ES"))
        val date = formatoEntrada.parse(hora)
        formatoSalida.format(date ?: return hora).uppercase() // <- Esto fuerza "AM" o "PM" en mayúscula
    } catch (e: Exception) {
        hora
    }
}


fun formatearFecha(fecha: String): String {
    return try {
        val formatoEntrada = java.text.SimpleDateFormat("dd/MM/yyyy")
        val formatoSalida = java.text.SimpleDateFormat("dd MMM", java.util.Locale("es", "ES"))
        val date = formatoEntrada.parse(fecha)
        formatoSalida.format(date ?: return fecha)
    } catch (e: Exception) {
        fecha
    }
}

