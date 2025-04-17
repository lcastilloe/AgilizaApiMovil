package com.example.agilizaapp.ui.components


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import java.util.Calendar

@Composable
fun AnadirPedido(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .fillMaxSize()
    ) {
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
            negrita = true // Cambia a false si quieres el texto normal
        )

        PantallaConFechaYHora()

    }


}

@Preview
@Composable
fun PreviwAnadirPedido(){
    AnadirPedido()
}

@Composable
fun FilaTablaAnadirPedido(
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
fun PantallaConFechaYHora() {
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

        SelectorFechaYHora(
            fechaSeleccionada = fecha,
            horaSeleccionada = hora,
            onFechaChange = { fecha = it },
            onHoraChange = { hora = it }
        )

}
@Composable
fun SelectorFechaYHora(
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



