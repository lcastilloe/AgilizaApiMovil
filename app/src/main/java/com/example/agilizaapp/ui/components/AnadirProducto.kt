package com.example.agilizaapp.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.agilizaapp.ui.data.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

@Composable
fun AnadirProducto(
    modifier: Modifier = Modifier,
    onProductoCreado: () -> Unit = {}
) {
    val context = LocalContext.current
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val storage = FirebaseStorage.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var inversion by remember { mutableStateOf("") }
    var margen by remember { mutableStateOf("") }
    var manoObra by remember { mutableStateOf("") }
    var utilidad by remember { mutableStateOf("") }
    var publicidad by remember { mutableStateOf("") }
    var venta by remember { mutableStateOf("") }

    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imagenUri = uri }

    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Producto",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp
                )
            }

            FilaTablaAnadirPedido1(listOf("Datos del Producto"), MaterialTheme.colorScheme.primaryContainer, true)
            CampoTextoPedido(
                labels = listOf("Código", "Nombre"),
                textos = listOf(codigo, nombre),
                onTextosChange = { index, value ->
                    if (index == 0) codigo = value else nombre = value
                },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )

            FilaTablaAnadirPedido1(listOf("Costos"), MaterialTheme.colorScheme.primaryContainer, true)
            CampoTextoPedido(
                labels = listOf("Inversión", "Margen"),
                textos = listOf(inversion, margen),
                onTextosChange = { index, value ->
                    if (index == 0) inversion = value else margen = value
                },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )
            CampoTextoPedido(
                labels = listOf("Mano de Obra", "Utilidad"),
                textos = listOf(manoObra, utilidad),
                onTextosChange = { index, value ->
                    if (index == 0) manoObra = value else utilidad = value
                },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )
            CampoTextoPedido(
                labels = listOf("Publicidad", "Venta"),
                textos = listOf(publicidad, venta),
                onTextosChange = { index, value ->
                    if (index == 0) publicidad = value else venta = value
                },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { imagePicker.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                Text("Seleccionar imagen")
            }

            imagenUri?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    if (uid != null && imagenUri != null) {
                        val nombreArchivo = UUID.randomUUID().toString()
                        val referencia = storage.reference.child("usuarios/$uid/productos/$nombreArchivo.jpg")
                        referencia.putFile(imagenUri!!)
                            .continueWithTask { tarea ->
                                if (!tarea.isSuccessful) throw tarea.exception ?: Exception("Falló carga de imagen")
                                referencia.downloadUrl
                            }.addOnSuccessListener { uri ->
                                val producto = Producto(
                                    id = nombreArchivo,
                                    codigo = codigo.toIntOrNull() ?: 0,
                                    nombre = nombre,
                                    valorInversion = inversion.toDoubleOrNull() ?: 0.0,
                                    valorMargen = margen.toDoubleOrNull() ?: 0.0,
                                    valorManoObra = manoObra.toDoubleOrNull() ?: 0.0,
                                    valorUtilidad = utilidad.toDoubleOrNull() ?: 0.0,
                                    valorPublicidad = publicidad.toDoubleOrNull() ?: 0.0,
                                    valorVenta = venta.toDoubleOrNull() ?: 0.0,
                                    fotoUrl = uri.toString()
                                )
                                firestore.collection("usuarios").document(uid).collection("productos")
                                    .document(nombreArchivo).set(producto)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Producto guardado con éxito", Toast.LENGTH_SHORT).show()
                                        onProductoCreado()
                                    }
                            }
                    }
                }
            ) {
                Text("Guardar Producto")
            }
        }
    }
}

