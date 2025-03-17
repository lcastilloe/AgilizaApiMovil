package com.example.agilizaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agilizaapp.R

@Composable
fun TopBar(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding() // 🔹 Mueve el TopBar debajo de la barra de estado
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono de menú (herramientas)
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menú",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // Logo centrado
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.nombrelogo),
            contentDescription = "Logo de Agiliza+",
            modifier = Modifier.height(60.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        // Imagen de perfil a la derecha
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Perfil de usuario",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onProfileClick() }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    TopBar(
        onMenuClick = { },
        onProfileClick = { }
    )
}
