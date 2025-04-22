package com.example.agilizaapp.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun DescripcionProyectoScreen() {
    // Estado para animación de desvanecimiento
    var visible by remember { mutableStateOf(false) }

    // Animación de desvanecimiento
    LaunchedEffect(Unit) {
        visible = true
    }

    // Scrollable column to allow the screen to scroll
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState), // Allows scrolling
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título de la sección
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
        ) {
            Text(
                text = "Proyecto Agiliza - Descripción",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Descripción del Proyecto
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
        ) {
            Text(
                text = """
                   Agiliza es una aplicación móvil diseñada para emprendedores que venden sus productos a través de redes sociales como WhatsApp e Instagram, ofreciendo una solución simple y eficaz para gestionar pedidos. En lugar de escribir pedidos en un cuaderno o usar métodos poco organizados, los usuarios pueden registrar rápidamente los pedidos, visualizar el estado de preparación y gestionar su catálogo de productos dentro de la aplicación. Agiliza facilita la organización y mejora el control de los negocios, permitiendo que los emprendedores se enfoquen en lo que realmente importa: crecer y vender.
                """.trimIndent(),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 32.dp),
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onBackground, // Colores según el tema
                textAlign = TextAlign.Justify // Justifica el texto
            )
        }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
        ) {
            Text(
                text = "Herramientas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Descripción del Proyecto
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
        ) {
            Text(
                text = """
El desarrollo de la aplicación Agiliza se llevó a cabo utilizando herramientas y tecnologías modernas para asegurar un rendimiento óptimo y una experiencia de usuario intuitiva. El proyecto fue desarrollado principalmente en Android con Jetpack Compose, una librería que permite crear interfaces de usuario de manera declarativa y más eficiente. Para la gestión de datos y la autenticación de usuarios, se utilizó Firebase, aprovechando Firebase Authentication para permitir el registro e inicio de sesión de los usuarios de manera segura. Además, se implementó Firestore para almacenar y gestionar en tiempo real los datos de los pedidos y productos, lo que asegura que los emprendedores puedan acceder a la información de manera inmediata y desde cualquier dispositivo. Se utilizó Coil para la carga eficiente de imágenes de productos, mejorando la experiencia visual de la aplicación. Con estas herramientas, se logró desarrollar una aplicación robusta, escalable y fácil de mantener, con la posibilidad de integrar nuevas funcionalidades en el futuro.
                """.trimIndent(),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 32.dp),
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onBackground, // Colores según el tema
                textAlign = TextAlign.Justify // Justifica el texto
            )
        }

        // Créditos
        // Créditos
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
        ) {
            Text(
                text = "Este proyecto fue realizado por:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + slideInVertically(initialOffsetY = { it / 2 }) // Añadimos animación de deslizamiento
        ) {
            Text(
                text = "Maria José Cabrera, Luisa Marcela Castillo E, Diego Ruales Guerrero",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp) // Se agrega espacio abajo para mejor visualización
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DescripcionProyectoScreenPreview() {
    DescripcionProyectoScreen()
}
