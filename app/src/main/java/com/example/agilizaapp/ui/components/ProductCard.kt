package com.example.agilizaapp.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import java.io.File
@Composable
fun ProductCard(
    number: String,
    title: String,
    price: String,
    imageUri: String,
    modifier: Modifier = Modifier
) {
    val painter = if (imageUri.isNotEmpty() && File(imageUri).exists()) {
        rememberAsyncImagePainter(model = File(imageUri))
    } else {
        null
    }


    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Sin imagen", color = Color.DarkGray)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(Color(0xFF98D8F8), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = "$number $title", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color(0xFF98D8F8), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(text = "$$price", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
