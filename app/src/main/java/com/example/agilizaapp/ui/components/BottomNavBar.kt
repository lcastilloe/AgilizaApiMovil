package com.example.agilizaapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = false,
                onClick = { navController.navigate("home") }
            )

            NavigationBarItem(
                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Pedidos") },
                label = { Text("Pedidos") },
                selected = false,
                onClick = { navController.navigate("pedidos") }
            )

            Spacer(Modifier.weight(1f))

            NavigationBarItem(
                icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Productos") },
                label = { Text("Productos") },
                selected = false,
                onClick = { navController.navigate("productos") }
            )

            NavigationBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Contabilidad") },
                label = { Text("Contabilidad") },
                selected = false,
                onClick = { navController.navigate("contabilidad") }
            )
        }

        // Botón flotante central (+)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { /* Acción para agregar */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Pedido")
            }
        }
    }
}