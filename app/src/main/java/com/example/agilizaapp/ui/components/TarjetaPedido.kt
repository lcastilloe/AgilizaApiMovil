package com.example.agilizaapp.ui.components

import android.R
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
import com.example.agilizaapp.ui.theme.AgilizaAppTheme


@Composable
fun TarjetaPedido(

    codigo: String,
    fecha: String,
    hora: String,
    estado: String,
    cliente: String,
    barrio: String,
    domicilio: String,
    productos: List<Producto>,
    total: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier // Permitir modificar tamaño desde el exterior
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = modifier
            .padding(8.dp)
            .width(170.dp)
            .height(200.dp)
    ){
        Column (){
            Row (modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center, // Centra horizontalmente
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$codigo - $fecha",
                    color = MaterialTheme.colorScheme.primary, // Color del texto
                    fontWeight = FontWeight.Bold, // Texto en negrita
                    fontSize = 16.sp // Tamaño del texto
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(20.dp),
                horizontalArrangement = Arrangement.Center,// Asegura que la fila ocupe todo el ancho disponible
                verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente

            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center ){
                    Box(
                        modifier = Modifier
                            // Hace que el Box ocupe la mitad del espacio disponible
                            .fillMaxHeight() // Para que tome toda la altura del Row si es necesario
                            .background(
                                color = MaterialTheme.colorScheme.primary, // Color de fondo del estado
                                shape = RoundedCornerShape(8.dp),
                                // Esquinas redondeadas

                            )
                            .padding(horizontal = 8.dp), // Espaciado interno
                        contentAlignment = Alignment.Center // Centra el contenido dentro del Box
                    ) {
                        // Contenido del Box (puede ser un icono, imagen, etc.)
                        Text(text = estado, Modifier.background(MaterialTheme.colorScheme.primary),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primaryContainer)
                    }
                }

                Text(
                    text = hora,
                    modifier = Modifier.weight(1f), // Hace que el texto ocupe la otra mitad del espacio
                    fontWeight = FontWeight.Bold, // Negrita
                    fontSize = 16.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
            }
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer,)
                .fillMaxWidth().height(20.dp),
                horizontalArrangement = Arrangement.Center, // Centra horizontalmente
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Cliente",
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Hace que el texto ocupe la otra mitad del espacio
                    fontWeight = FontWeight.Bold, // Negrita
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
                Text(
                    text = "Barrio",
                    modifier = Modifier.weight(1f), // Hace que el texto ocupe la otra mitad del espacio
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Negrita
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
                Text(
                    text = "Domicilio",
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Hace que el texto ocupe la otra mitad del espacio
                    fontWeight = FontWeight.Bold, // Negrita
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )


            }
            Row(modifier = Modifier.fillMaxWidth().height(20.dp),
                horizontalArrangement = Arrangement.Center, // Centra horizontalmente
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = cliente,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Hace que el texto ocupe la otra mitad del espacio
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
                Text(
                    text = barrio,
                    modifier = Modifier.weight(1f), // Hace que el texto ocupe la otra mitad del espaci
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Negrita
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
                Text(
                    text = domicilio,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Hace que el texto ocupe la otra mitad del espacio
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
            }
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer,)
                .fillMaxWidth().height(20.dp),
                horizontalArrangement = Arrangement.Center, // Centra horizontalmente
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Producto",
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Hace que el texto ocupe la otra mitad del espacio
                    fontWeight = FontWeight.Bold, // Negrita
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
                Text(
                    text = "Estado",
                    modifier = Modifier.weight(1f), // Hace que el texto ocupe la otra mitad del espacio
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Negrita
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
                Text(
                    text = "Precio",
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,// Hace que el texto ocupe la otra mitad del espacio
                    fontWeight = FontWeight.Bold, // Negrita
                    fontSize = 10.sp, // Tamaño del texto
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(70.dp),
                horizontalArrangement = Arrangement.Center,// Asegura que la fila ocupe todo el ancho disponible
                verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente

            ) {}
            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                horizontalArrangement = Arrangement.Center,// Asegura que la fila ocupe todo el ancho disponible
                verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente
            ) {
                Text(
                    text = "TOTAL: $total",
                    color = MaterialTheme.colorScheme.primary, // Color del texto
                    fontWeight = FontWeight.Bold, // Texto en negrita
                    fontSize = 16.sp // Tamaño del texto
                )
            }


        }

    }
}
@Preview
@Composable
fun PreviewTrajetaPedido(){
    AgilizaAppTheme {
        TarjetaPedido(
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
}