package com.example.agilizaapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.agilizaapp.ui.components.AnadirPedido1
import com.example.agilizaapp.ui.components.AnadirPedidoConProductos
import com.example.agilizaapp.ui.components.AnadirProducto
import com.example.agilizaapp.ui.components.BottomNavBar
import com.example.agilizaapp.ui.components.TopBar
import com.example.agilizaapp.ui.navigation.Screen
import com.example.agilizaapp.ui.screens.AgendaScreen
import com.example.agilizaapp.ui.screens.DescripcionProyectoScreen
import com.example.agilizaapp.ui.screens.HomeScreen
import com.example.agilizaapp.ui.screens.LoginScreen
import com.example.agilizaapp.ui.screens.ProductGrid
import com.example.agilizaapp.ui.screens.PantallaCarga
import com.example.agilizaapp.ui.theme.AgilizaAppTheme
import com.example.agilizaapp.ui.viewmodels.HomeViewModel
import com.example.agilizaapp.ui.viewmodels.LoginScreenViewModel
import com.example.agilizaapp.ui.viewmodels.SharedPedidoViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permiso de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

        enableEdgeToEdge()

        setContent {
            AgilizaAppTheme {
                // Estados para splash, autenticación y navegación
                var mostrarCarga by remember { mutableStateOf(true) }
                var estaLogueado by remember { mutableStateOf(false) }
                var currentScreen by remember { mutableStateOf(Screen.LOGIN) }
                var showProfileDialog by remember { mutableStateOf(false) }

                // ViewModels compartidos
                val sharedPedidoVM: SharedPedidoViewModel = viewModel()
                val homeViewModel: HomeViewModel = viewModel()
                val loginViewModel: LoginScreenViewModel = viewModel()

                // Datos de usuario
                val userPhotoUrl by loginViewModel.userPhotoUrl
                val userName by loginViewModel.userName

                // Efecto para mostrar pantalla de carga
                LaunchedEffect(Unit) {
                    delay(3000)
                    mostrarCarga = false
                }

                if (mostrarCarga) {
                    PantallaCarga(onTimeout = { mostrarCarga = false })
                } else {
                    if (!estaLogueado) {
                        // Login inicial
                        LoginScreen(
                            onLoginSuccess = {
                                homeViewModel.cargarPedidos()
                                estaLogueado = true
                                currentScreen = Screen.PEDIDOS
                            }
                        )
                    } else {
                        // App principal
                        Scaffold(
                            topBar = {
                                TopBar(
                                    onMenuClick = { /* TODO menú */ },
                                    onProfileClick = { showProfileDialog = true },
                                    userPhotoUrl = userPhotoUrl,
                                    userName = userName
                                )
                            },
                            bottomBar = {
                                BottomNavBar(
                                    selectedScreen = currentScreen,
                                    onItemSelected = { screen -> currentScreen = screen }
                                )
                            },
                            contentWindowInsets = WindowInsets.safeDrawing
                        ) { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) {
                                when (currentScreen) {
                                    Screen.LOGIN -> LoginScreen(onLoginSuccess = {})
                                    Screen.PRODUCTOS -> ProductGrid(onClickAnadirProducto = {
                                        currentScreen = Screen.ANADIR_PRODUCTO
                                    })
                                    Screen.PEDIDOS -> HomeScreen()
                                    Screen.AGENDA -> AgendaScreen()
                                    Screen.CREDITOS -> DescripcionProyectoScreen()
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
                                                    homeViewModel.cargarPedidos()
                                                    currentScreen = Screen.PEDIDOS
                                                }
                                            )
                                        } else {
                                            Text("Error: No se encontró la información del pedido")
                                        }
                                    }
                                    Screen.ANADIR_PRODUCTO -> AnadirProducto(onProductoCreado = {
                                        currentScreen = Screen.PRODUCTOS
                                    })

                                    else -> HomeScreen()
                                }
                            }
                        }
                        // Diálogo de perfil
                        if (showProfileDialog) {
                            ShowProfileDialog(
                                userName = userName,
                                userPhotoUrl = userPhotoUrl,
                                onLogout = {
                                    FirebaseAuth.getInstance().signOut()
                                    loginViewModel.clearUserData()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        estaLogueado = false
                                        currentScreen = Screen.LOGIN
                                    }, 1000)
                                    showProfileDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowProfileDialog(
        userName: String,
        userPhotoUrl: String,
        onLogout: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Bienvenido, $userName") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(userPhotoUrl),
                        contentDescription = "Perfil de usuario",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Text("Nombre: $userName")
                }
            },
            confirmButton = {
                TextButton(onClick = onLogout) { Text("Cerrar sesión") }
            },
            dismissButton = {
                TextButton(onClick = {}) { Text("Cancelar") }
            }
        )
    }
}