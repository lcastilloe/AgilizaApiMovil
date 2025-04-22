package com.example.agilizaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun PantallaCarga(onTimeout: () -> Unit) {
    // Carga la animación Lottie desde assets/animacion.json
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("animacion.json"))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    // Retardo de 3 segundos para simular la carga
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    // UI de la pantalla de carga
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Agiliza+",
                fontSize = 30.sp,
                color = Color(0xFF6200EA), // Puedes usar el color primary que desees
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }
    }
}