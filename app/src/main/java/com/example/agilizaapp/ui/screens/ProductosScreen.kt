package com.example.agilizaapp.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agilizaapp.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight


@Composable
fun ProductGrid() {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ProductCard(
                number = "01",
                title = "Cuadro Plutón Grande",
                price = "$40.000",
                imageRes = R.drawable.p1,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ProductCard(
                number = "02",
                title = "Cuadro Peq Con Diploma",
                price = "$50.000",
                imageRes = R.drawable.p2,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            ProductCard(
                number = "03",
                title = "Alcancía Plutón",
                price = "$45.000",
                imageRes = R.drawable.p3,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ProductCard(
                number = "04",
                title = "Libro '50 citas juntos'",
                price = "$68.000",
                imageRes = R.drawable.p4,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            ProductCard(
                number = "05",
                title = "Calendario",
                price = "$35.000",
                imageRes = R.drawable.p5,
                modifier = Modifier
                    .weight(1f)

            )
        }
    }
}
@Composable
fun ProductCard(number: String, title: String, price: String, imageRes: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        // Texto superior (Número y título)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(Color(0xFF98D8F8), shape = RoundedCornerShape(12.dp)) // Color azul claro
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$number $title",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        // Texto inferior (Precio)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color(0xFF98D8F8), shape = RoundedCornerShape(12.dp)) // Color azul claro
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = price,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductosScreen() {

    ProductGrid()

}