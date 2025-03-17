package com.example.agilizaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PedidoCard(
    codigo: String,
    fecha: String,
    hora: String,
    estado: String,
    cliente: String,
    barrio: String,
    domicilio: String,
    productos: List<Producto>,
    total: String,
    backgroundColor: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .size(250.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween // 🔹 Separa el contenido y el total
        ) {
            Column {
                // 🔹 Encabezado con código, fecha y estado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$codigo - $fecha", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadge(status = estado)
                    Text(hora, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Detalles del pedido
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    InfoText(title = "Cliente", value = cliente)
                    InfoText(title = "Barrio", value = barrio)
                    InfoText(title = "Domicilio", value = domicilio)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Lista de productos
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Producto", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Estado", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Precio", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Divider()

                productos.forEach { producto ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(producto.codigo, fontSize = 12.sp)
                        StatusBadge(status = producto.estado)
                        Text(producto.precio, fontSize = 12.sp)
                    }
                }
            }

            // 🔹 Total del pedido (siempre abajo y centrado)
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "TOTAL: $total",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


// 🔹 Composable para mostrar el estado con color
@Composable
fun StatusBadge(status: String) {
    val statusColor = when (status) {
        "Listo" -> Color(0xFF006C99) // Azul
        "Preparación" -> Color(0xFFFFC107) // Amarillo
        "Inicio" -> Color(0xFFF44336) // Rojo
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .background(statusColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(status, color = Color.White, fontSize = 12.sp)
    }
}

// 🔹 Composable para mostrar información en la parte superior
@Composable
fun InfoText(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(value, fontSize = 12.sp)
    }
}

// 🔹 Modelo de datos para productos
data class Producto(val codigo: String, val estado: String, val precio: String)

// 🔹 Preview con datos de prueba
@Preview(showBackground = true)
@Composable
fun PreviewPedidoCard() {
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
