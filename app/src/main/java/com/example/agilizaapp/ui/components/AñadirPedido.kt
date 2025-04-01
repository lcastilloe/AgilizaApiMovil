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
            columnas = listOf("Datos de entrega"),
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            negrita = true // Cambia a false si quieres el texto normal
        )
        FilaTablaAnadirPedido(
            columnas = listOf("Fecha","Hora"),
            backgroundColor = MaterialTheme.colorScheme.inversePrimary,
            negrita = true // Cambia a false si quieres el texto normal
        )
        FilaTablaAnadirPedido(
            columnas = listOf("Fecha","Hora"),
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            negrita = false // Cambia a false si quieres el texto normal
        )

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
                textAlign = androidx.compose.ui.text.style.TextAlign.Center, // Centra el texto dentro de su espacio
                fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal, // Aplica negrita si el usuario lo solicita
                fontSize = 18.sp, // Tamaño del texto
                color = MaterialTheme.colorScheme.primary // Color del texto
            )
        }
    }
}
