package com.example.agilizaapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agilizaapp.ui.navigation.Screen
import com.example.agilizaapp.ui.theme.AgilizaAppTheme

@Composable
fun BottomNavBar(selectedScreen: Screen,
                 onItemSelected: (Screen) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Agenda") },
            label = { Text("Agenda") },
            selected = false,
            onClick = {onItemSelected(Screen.AGENDA) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Pedidos") }, // 🔹 Icono corregido
            label = { Text("Pedidos") },
            selected = true,
            onClick = {onItemSelected(Screen.PEDIDOS)},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(Modifier.weight(1f))

        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Productos") },
            label = { Text("Producto") },
            selected = false,
            onClick = { onItemSelected(Screen.PRODUCTOS)},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Contabilidad") },
            label = { Text("Creditos") },
            selected = false,
            onClick = { onItemSelected(Screen.CREDITOS) }, // Cambio aquí
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

    }

    // 🔹 Botón flotante central (+)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = {onItemSelected(Screen.ANADIR_PEDIDO) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar Pedido")
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun PreviewBottomNavBar() {
    AgilizaAppTheme  { // 🔹 Encapsulamos el Preview en MaterialTheme
        BottomNavBar()
    }
}
*/