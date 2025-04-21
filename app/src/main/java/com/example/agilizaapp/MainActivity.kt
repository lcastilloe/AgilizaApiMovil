package com.example.agilizaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.agilizaapp.ui.components.AnadirPedido1
import com.example.agilizaapp.ui.components.AnadirPedidoConProductos
import com.example.agilizaapp.ui.components.AnadirProducto
import com.example.agilizaapp.ui.components.BottomNavBar
import com.example.agilizaapp.ui.components.TopBar
import com.example.agilizaapp.ui.navigation.Screen
import com.example.agilizaapp.ui.screens.HomeScreen
import com.example.agilizaapp.ui.screens.LoginScreen
import com.example.agilizaapp.ui.screens.ProductGrid
import com.example.agilizaapp.ui.theme.AgilizaAppTheme
import com.example.agilizaapp.ui.viewmodels.HomeViewModel
import com.example.agilizaapp.ui.viewmodels.LoginScreenViewModel
import com.example.agilizaapp.ui.viewmodels.SharedPedidoViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AgilizaAppTheme {
                val currentUser = FirebaseAuth.getInstance().currentUser
                var currentScreen by remember {
                    mutableStateOf(
                        if (currentUser == null) Screen.LOGIN else Screen.PEDIDOS
                    )
                }

                val sharedPedidoVM: SharedPedidoViewModel = viewModel()
                val homeViewModel: HomeViewModel = viewModel()
                val loginViewModel: LoginScreenViewModel = viewModel()

                val userPhotoUrl = loginViewModel.userPhotoUrl.value
                val userName = loginViewModel.userName.value

                var showDialog by remember { mutableStateOf(false) }

                // Mostrar el Scaffold con los elementos de la interfaz
                Scaffold(
                    topBar = {
                        if (currentScreen != Screen.LOGIN) {
                            TopBar(
                                onMenuClick = { /* TODO */ },
                                onProfileClick = {
                                    showDialog = true // Mostrar el diálogo cuando se hace clic en el perfil
                                },
                                userPhotoUrl = userPhotoUrl,
                                userName = userName
                            )
                        }
                    },
                    bottomBar = {
                        if (currentScreen != Screen.LOGIN) {
                            BottomNavBar(
                                selectedScreen = currentScreen,
                                onItemSelected = { screen ->
                                    currentScreen = screen
                                }
                            )
                        }
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        when (currentScreen) {
                            Screen.LOGIN -> LoginScreen(
                                onLoginSuccess = {
                                    homeViewModel.cargarPedidos() // ← Cargar pedidos al iniciar sesión
                                    currentScreen = Screen.PEDIDOS
                                }
                            )

                            Screen.PRODUCTOS -> ProductGrid(
                                onClickAnadirProducto = {
                                    currentScreen = Screen.ANADIR_PRODUCTO
                                }
                            )

                            Screen.PEDIDOS -> HomeScreen()

                            Screen.ANADIR_PEDIDO -> AnadirPedido1(
                                onContinuar = { pedidoTemporal, codigo ->
                                    sharedPedidoVM.pedidoTemporal = pedidoTemporal
                                    sharedPedidoVM.codigoGenerado = codigo
                                    currentScreen = Screen.ANADIR_PEDIDO_CON_PRODUCTOS
                                }
                            )

                            Screen.ANADIR_PEDIDO_CON_PRODUCTOS -> {
                                val pedidoTemporal = sharedPedidoVM.pedidoTemporal
                                val codigo = sharedPedidoVM.codigoGenerado

                                if (pedidoTemporal != null) {
                                    AnadirPedidoConProductos(
                                        codigo = codigo,
                                        pedidoTemporal = pedidoTemporal,
                                        onPedidoGuardado = {
                                            homeViewModel.cargarPedidos() // ← FORZAR RECARGA
                                            currentScreen = Screen.PEDIDOS
                                        }
                                    )
                                } else {
                                    Text("Error: No se encontró la información del pedido")
                                }
                            }
                            Screen.ANADIR_PRODUCTO -> AnadirProducto(
                                onProductoCreado = {
                                    currentScreen = Screen.PRODUCTOS
                                }
                            )


                            else -> HomeScreen()
                        }
                    }
                }
                // Mostrar el diálogo solo cuando showDialog es true
                if (showDialog) {
                    showProfileDialog(userName, userPhotoUrl) {
                        // Cerrar sesión y volver a la pantalla de login
                        FirebaseAuth.getInstance().signOut()
                        currentScreen = Screen.LOGIN
                        showDialog = false // Ocultar el diálogo
                    }
                }
            }
        }
    }
    // Función para mostrar el diálogo con el nombre del usuario y el botón de cerrar sesión
    @Composable
    private fun showProfileDialog(userName: String, userPhotoUrl: String, onLogout: () -> Unit) {
        // Mostrar un diálogo con el nombre del usuario, foto y botón de cerrar sesión
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text("Bienvenido, $userName")
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(userPhotoUrl),
                        contentDescription = "Perfil de usuario",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Text("Nombre: $userName")
                }
            },
            confirmButton = {
                TextButton(onClick = onLogout) {
                    Text("Cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { }) {
                    Text("Cancelar")
                }
            }
        )
    }

}
