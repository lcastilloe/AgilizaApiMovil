package com.example.agilizaapp.ui.components


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agilizaapp.model.PedidoTemporal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar


@Composable
fun AnadirPedido1(
    modifier: Modifier = Modifier,
    onContinuar: (PedidoTemporal, String) -> Unit

) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    var nombreCliente by remember { mutableStateOf("") }
    var numeroCliente by remember { mutableStateOf("") }
    var nombreDestinatario by remember { mutableStateOf("") }
    var numeroDestinatario by remember { mutableStateOf("") }
    var direccionEntrega by remember { mutableStateOf("") }
    var barrioEntrega by remember { mutableStateOf("") }
    var tarjetaDePara by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var codigoPedido by remember { mutableStateOf("...") }

    // Obtener código único al iniciar
    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("usuarios")
                .document(uid)
                .collection("pedidos")
                .get()
                .addOnSuccessListener { result ->
                    val codigos = result.mapNotNull { it.getString("codigo") }
                    val letras = ('A'..'Z')
                    var generado = false
                    for (letra in letras) {
                        for (numero in 1..99) {
                            val posible = letra + numero.toString().padStart(2, '0')
                            if (posible !in codigos) {
                                codigoPedido = posible
                                generado = true
                                break
                            }
                        }
                        if (generado) break
                    }
                }
        }
    }

    if (mostrarDialogoError) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoError = false },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoError = false }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Campos obligatorios incompletos") },
            text = {
                Text("Por favor completa todos los campos obligatorios: Fecha, Hora, Cliente, Número, Tarjeta De/Para.")
            }
        )
    }

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
                    text = codigoPedido,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp
                )
            }

            FilaTablaAnadirPedido1(listOf("Datos de Pedido"), MaterialTheme.colorScheme.primaryContainer, true)
            PantallaConFechaYHora(fecha, hora, { fecha = it }, { hora = it })

            FilaTablaAnadirPedido1(listOf("Datos de Cliente"), MaterialTheme.colorScheme.primaryContainer, true)
            CampoTextoPedido(
                labels = listOf("Número", "Nombre"),
                textos = listOf(nombreCliente, numeroCliente),
                onTextosChange = { index, value ->
                    if (index == 0) nombreCliente = value else numeroCliente = value
                },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )

            FilaTablaAnadirPedido1(listOf("Datos de Destinatario"), MaterialTheme.colorScheme.primaryContainer, true)
            CampoTextoPedido(
                labels = listOf("Nombre", "Numero"),
                textos = listOf(nombreDestinatario, numeroDestinatario),
                onTextosChange = { index, value ->
                    if (index == 0) nombreDestinatario = value else numeroDestinatario = value
                },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )

            FilaTablaAnadirPedido1(listOf("Datos de Entrega"), MaterialTheme.colorScheme.primaryContainer, true)
            CampoTextoPedido(
                labels = listOf("Direccion", "Barrio"),
                textos = listOf(direccionEntrega, barrioEntrega),
                onTextosChange = { index, value ->
                    if (index == 0) direccionEntrega = value else barrioEntrega = value
                },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )

            FilaTablaAnadirPedido1(listOf("Tarjeta De Y Para"), MaterialTheme.colorScheme.primaryContainer, true)
            CampoTextoPedido(
                labels = listOf("Obligatorio para diferenciar el regalo"),
                textos = listOf(tarjetaDePara),
                onTextosChange = { _, value -> tarjetaDePara = value },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )

            Button(
                onClick = {
                    if (fecha.isBlank() || hora.isBlank() || nombreCliente.isBlank() || numeroCliente.isBlank() || tarjetaDePara.isBlank()) {
                        mostrarDialogoError = true
                    } else {
                        val pedidoTemporal = PedidoTemporal(
                            nombreCliente,
                            numeroCliente,
                            nombreDestinatario,
                            numeroDestinatario,
                            direccionEntrega,
                            barrioEntrega,
                            tarjetaDePara,
                            fecha,
                            hora
                        )
                        println("Pedido temporal guardado: $pedidoTemporal")
                        onContinuar(pedidoTemporal,codigoPedido)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Continuar")
            }
            /*Button(
                onClick = {
                    crearProductosDePruebaPorUsuario()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Crear productos de ejemplo")
            }*/


        }
    }
}





@Composable
fun FilaTablaAnadirPedido1(
    columnas: List<String>, // Lista de textos para cada columna
    backgroundColor: Color, // Color de fondo de la fila
    negrita: Boolean // Indica si el texto debe ser negrita
) {
    Row(
        modifier = Modifier
            .background(backgroundColor) // Aplica el color de fondo
            .fillMaxWidth()
            .height(24.dp),
        horizontalArrangement = Arrangement.Center, // Centra los elementos horizontalmente
        verticalAlignment = Alignment.CenterVertically // Centra verticalmente los textos
    ) {
        columnas.forEach { texto ->
            Text(
                text = texto,
                modifier = Modifier.weight(1f), // Hace que todas las columnas ocupen el mismo espacio
                textAlign = TextAlign.Center, // Centra el texto dentro de su espacio
                fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal, // Aplica negrita si el usuario lo solicita
                fontSize = 18.sp, // Tamaño del texto
                color = MaterialTheme.colorScheme.primary // Color del texto
            )

        }
    }
}

@Composable
fun PantallaConFechaYHora(
    fecha: String,
    hora: String,
    onFechaChange: (String) -> Unit,
    onHoraChange: (String) -> Unit
) {
    SelectorFechaYHora1(
        fechaSeleccionada = fecha,
        horaSeleccionada = hora,
        onFechaChange = onFechaChange,
        onHoraChange = onHoraChange
    )
}

@Composable
fun SelectorFechaYHora1(
    fechaSeleccionada: String,
    horaSeleccionada: String,
    onFechaChange: (String) -> Unit,
    onHoraChange: (String) -> Unit
) {
    val context = LocalContext.current

    // Manejador de fecha
    val calendario = Calendar.getInstance()
    val year = calendario.get(Calendar.YEAR)
    val month = calendario.get(Calendar.MONTH)
    val day = calendario.get(Calendar.DAY_OF_MONTH)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            onHoraChange(String.format("%02d:%02d", hour, minute))
        },
        calendario.get(Calendar.HOUR_OF_DAY),
        calendario.get(Calendar.MINUTE),
        false
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            onFechaChange("$d/${m + 1}/$y")
        },
        year,
        month,
        day
    )
    // Personalizar los colores

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Fecha:",
            //modifier = Modifier.weight(1f), // Hace que todas las columnas ocupen el mismo espacio
            textAlign = TextAlign.Center, // Centra el texto dentro de su espacio
            fontWeight = if (true) FontWeight.Bold else FontWeight.Normal, // Aplica negrita si el usuario lo solicita
            fontSize = 18.sp, // Tamaño del texto
            color = MaterialTheme.colorScheme.primary // Color del texto
        )
        OutlinedButton(onClick = { datePickerDialog.show() }, modifier = Modifier.weight(1f)) {
            Text(text = fechaSeleccionada.ifBlank { "Seleccionar" },
                fontSize = 10.sp)
        }
        Text(
            text = "Hora:",
            //modifier = Modifier.weight(1f), // Hace que todas las columnas ocupen el mismo espacio
            textAlign = TextAlign.Center, // Centra el texto dentro de su espacio
            fontWeight = if (true) FontWeight.Bold else FontWeight.Normal, // Aplica negrita si el usuario lo solicita
            fontSize = 18.sp, // Tamaño del texto
            color = MaterialTheme.colorScheme.primary // Color del texto
        )

        OutlinedButton(onClick = { timePickerDialog.show() }, modifier = Modifier.weight(1f)) {
            Text(text = horaSeleccionada.ifBlank { "Seleccionar" },
                fontSize = 9.sp,)
        }

    }
}

@Composable
fun CampoTextoPedido(
    labels: List<String>,
    textos: List<String>,
    onTextosChange: (index: Int, nuevoTexto: String) -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        labels.forEachIndexed { index, label ->
            OutlinedTextField(
                value = textos.getOrNull(index) ?: "",
                onValueChange = { nuevoValor -> onTextosChange(index, nuevoValor) },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp // Tamaño del label
                    )
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp), // Tamaño del texto que se escribe
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp), // Altura más pequeña
                singleLine = true,
                shape = RoundedCornerShape(30.dp)
            )
        }
    }
}



//p---------------------------------------------------------roductos de prueba


fun crearProductosDePruebaPorUsuario() {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val productos = listOf(
        mapOf(
            "codigo" to 3,
            "nombre" to "Recuerdos",
            "valorInversion" to 3000.0,
            "valorMargen" to 2000.0,
            "valorManoObra" to 1000.0,
            "valorUtilidad" to 1500.0,
            "valorPublicidad" to 500.0,
            "valorVenta" to 8000.0
        ),
        mapOf(
            "codigo" to 4,
            "nombre" to "Cuadro",
            "valorInversion" to 2000.0,
            "valorMargen" to 1000.0,
            "valorManoObra" to 500.0,
            "valorUtilidad" to 1000.0,
            "valorPublicidad" to 300.0,
            "valorVenta" to 4800.0
        )
    )

    val db = FirebaseFirestore.getInstance()
    val coleccion = db.collection("usuarios").document(uid).collection("productos")

    productos.forEach { producto ->
        coleccion.add(producto)
            .addOnSuccessListener {
                println("✅ Producto agregado para usuario $uid: ${producto["nombre"]}")
            }
            .addOnFailureListener {
                println("❌ Error al agregar producto: ${it.message}")
            }
    }
}
