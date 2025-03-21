package com.example.agilizaapp.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.agilizaapp.ui.Producto
import com.example.agilizaapp.ui.theme.AgilizaAppTheme

@Composable
fun TarjetaPedido(
    codigo: String,
    fecha: String,
    hora: String,
    cliente: String,
    barrio: String,
    domicilio: String,
    productosIniciales: List<Producto>,
    total: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    // Asegurar que todos los productos comiencen en "Inicio"
    val productos = remember {
        mutableStateListOf(*productosIniciales.map { it.copy(estado = "Inicio") }.toTypedArray())
    }

    // Estado mutable para el estado del pedido
    var estadoPedido by remember { mutableStateOf(calcularEstadoPedido(productos.map { it.estado })) }

    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
            .padding(8.dp)
            .width(170.dp)
            .height(230.dp)
    ) {
        Column {
            // Título con código y fecha
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$codigo - $fecha",
                    color = MaterialTheme.colorScheme.primary,
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
                            .background(color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 5.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = estadoPedido,
                            fontSize = 12.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Text(
                    text = hora,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Tabla de encabezados

            FilaTabla(
                columnas = listOf("Cliente", "Barrio", "Domicilio"),
                backgroundColor = MaterialTheme.colorScheme.inversePrimary,
                negrita = true // Cambia a false si quieres el texto normal
            )
            FilaTabla(
                columnas = listOf(cliente, barrio, domicilio),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                negrita = false // Cambia a false si quieres el texto normal
            )
            FilaTabla(
                columnas = listOf("Producto", "Estado", "Precio"),
                backgroundColor = MaterialTheme.colorScheme.inversePrimary,
                negrita = true // Cambia a false si quieres el texto normal
            )

            // Tabla de productos con estados interactivos
            productos.forEachIndexed { index, producto ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(30.dp)
                ) {
                    FilaProducto(
                        elementos = listOf(
                            { Text(producto.codigo, fontSize = 10.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.primary) },
                            {
                                BoxEstado(estadoInicial = productos[index].estado) { nuevoEstado ->
                                    productos[index] = productos[index].copy(estado = nuevoEstado)
                                    estadoPedido = calcularEstadoPedido(productos.map { it.estado }) // Actualiza el estado del pedido
                                }
                            },
                            { Text(producto.precio, fontSize = 10.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.primary) }
                        ),
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        negrita = false
                    )
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
                Producto("01", "Inicio", "$40.000"),
                Producto("02", "Preparación", "$50.000"),
                Producto("03", "Listo", "$60.000")
            ),
            total = "$150.000",
            backgroundColor = Color(0xFFD0F0FF)
        )
    }
}

@Composable
fun FilaTabla(
    columnas: List<String>, // Lista de textos para cada columna
    backgroundColor: Color, // Color de fondo de la fila
    negrita: Boolean // Indica si el texto debe ser negrita
) {
    Row(
        modifier = Modifier
            .background(backgroundColor) // Aplica el color de fondo
            .fillMaxWidth()
            .height(20.dp),
        horizontalArrangement = Arrangement.Center, // Centra los elementos horizontalmente
        verticalAlignment = Alignment.CenterVertically // Centra verticalmente los textos
    ) {
        columnas.forEach { texto ->
            Text(
                text = texto,
                modifier = Modifier.weight(1f), // Hace que todas las columnas ocupen el mismo espacio
                textAlign = androidx.compose.ui.text.style.TextAlign.Center, // Centra el texto dentro de su espacio
                fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal, // Aplica negrita si el usuario lo solicita
                fontSize = 10.sp, // Tamaño del texto
                color = MaterialTheme.colorScheme.primary // Color del texto
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
