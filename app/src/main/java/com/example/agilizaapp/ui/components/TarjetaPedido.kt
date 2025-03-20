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
import com.example.agilizaapp.ui.theme.AgilizaAppTheme

@Composable
fun TarjetaPedido(
    codigo: String,
    fecha: String,
    hora: String,
    cliente: String,
    barrio: String,
    domicilio: String,
    total: String,
    backgroundColor: Color,
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        modifier = modifier
            .padding(8.dp)
            .width(170.dp)
    ){
                .fillMaxWidth(),
                Text(
                    text = "$codigo - $fecha",
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(20.dp),
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center ){
                    Box(
                        modifier = Modifier
                    ) {
                Text(
                )
            }
                Text(
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                )


                )
                )
                )
            Row(

            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp),
            ) {
                Text(
                    text = "TOTAL: $total",
                )
            }
        }

    }
}
@Preview
@Composable
    AgilizaAppTheme {
        TarjetaPedido(
            codigo = "A10",
            fecha = "11 Febrero",
            hora = "10:30 am",
            cliente = "Mateo",
            barrio = "Santa Clara",
            domicilio = "$7.000",
            ),
        )
    }
}