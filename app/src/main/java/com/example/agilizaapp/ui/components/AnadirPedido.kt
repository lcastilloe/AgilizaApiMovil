package com.example.agilizaapp.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agilizaapp.ui.viewmodels.AnadirPedidoViewModel
import java.util.Calendar
import java.util.Locale

@Composable
fun AnadirPedido(
    modifier: Modifier = Modifier
) {
    val viewModel: AnadirPedidoViewModel = viewModel()
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

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
                    text = "A10",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp
                )
            }

            FilaTablaAnadirPedido(
                columnas = listOf("Datos de pedido"),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                negrita = true
            )

            SelectorFechaYHora(
                fechaSeleccionada = fecha,
                horaSeleccionada = hora,
                onFechaChange = { fecha = it },
                onHoraChange = { hora = it }
            )

            Button(
                onClick = {
                    viewModel.guardarPedidoDeEjemplo(
                        fecha = fecha,
                        hora = hora,
                        onSuccess = {
                            Log.d("Pedido", "Guardado exitosamente")
                        },
                        onError = {
                            Log.e("Pedido", "Error al guardar", it)
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Guardar pedido de ejemplo")
            }
        }
    }
}

@Preview
@Composable
fun PreviwAnadirPedido(){
    AnadirPedido()
}

@Composable
fun FilaTablaAnadirPedido(
    columnas: List<String>,
    backgroundColor: Color,
    negrita: Boolean
) {
    Row(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        columnas.forEach { texto ->
            Text(
                text = texto,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SelectorFechaYHora(
    fechaSeleccionada: String,
    horaSeleccionada: String,
    onFechaChange: (String) -> Unit,
    onHoraChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendario = Calendar.getInstance()

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            onHoraChange(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
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
        calendario.get(Calendar.YEAR),
        calendario.get(Calendar.MONTH),
        calendario.get(Calendar.DAY_OF_MONTH)
    )

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
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        OutlinedButton(onClick = { datePickerDialog.show() }, modifier = Modifier.weight(1f)) {
            Text(text = fechaSeleccionada.ifBlank { "Seleccionar" }, fontSize = 10.sp)
        }

        Text(
            text = "Hora:",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        OutlinedButton(onClick = { timePickerDialog.show() }, modifier = Modifier.weight(1f)) {
            Text(text = horaSeleccionada.ifBlank { "Seleccionar" }, fontSize = 9.sp)
        }
    }
}