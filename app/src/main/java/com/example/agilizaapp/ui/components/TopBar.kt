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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.agilizaapp.R
import com.example.agilizaapp.ui.theme.AgilizaAppTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TopBar(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    userPhotoUrl: String, // Foto del usuario
    userName: String // Nombre del usuario
) {
    var isProfileDialogVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menú",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Mostrar el logo
        Image(
            painter = painterResource(id = R.drawable.nombrelogo),
            contentDescription = "Logo de Agiliza+",
            modifier = Modifier.height(60.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Imagen de perfil
        Image(
            painter = rememberImagePainter(userPhotoUrl),
            contentDescription = "Perfil de usuario",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onProfileClick() }
        )

        // Si es necesario, aquí se puede mostrar el nombre del usuario y el botón de cerrar sesión
        if (isProfileDialogVisible) {
            // Mostrar el diálogo con el nombre y botón para cerrar sesión
            AlertDialog(
                onDismissRequest = { isProfileDialogVisible = false },
                title = { Text("Bienvenido, $userName") },
                confirmButton = {
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut() // Cerrar sesión
                        isProfileDialogVisible = false
                    }) {
                        Text("Cerrar sesión")
                    }
                }
            )
        }
    }
}

