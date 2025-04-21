package com.example.agilizaapp.ui.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.agilizaapp.ui.data.Producto
import com.example.agilizaapp.ui.viewmodels.ProductoViewModel
import java.io.File
import java.util.*

@Composable
fun AnadirProducto(
    modifier: Modifier = Modifier,
    onProductoCreado: () -> Unit = {},
    productoViewModel: ProductoViewModel = viewModel()
) {
    val context = LocalContext.current

    // Obtén la lista de productos actuales
    val productos = productoViewModel.productos.collectAsState().value

    // Generar el nuevo código basado en los productos existentes
    val nuevoCodigo = generarCodigoAutomatico(productos)

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
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            // Encabezado
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

            // Mostrar código generado automáticamente debajo de "Datos del Producto"
            FilaTablaAnadirPedido1(listOf("Datos del Producto"), MaterialTheme.colorScheme.primaryContainer, true)
            Text(
                text = "Código generado: $nuevoCodigo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

            // Solo campo de texto para nombre
            CampoTextoPedido(
                labels = listOf("Nombre"),
                textos = listOf(nombre),
                onTextosChange = { _, value -> nombre = value },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )

            FilaTablaAnadirPedido1(listOf("Costos"), MaterialTheme.colorScheme.primaryContainer, true)
            CampoTextoPedido(
                labels = listOf("Inversión", "Margen"),
                textos = listOf(inversion, margen),
                onTextosChange = { index, value -> if (index == 0) inversion = value else margen = value },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )
            CampoTextoPedido(
                labels = listOf("Mano de Obra", "Utilidad"),
                textos = listOf(manoObra, utilidad),
                onTextosChange = { index, value -> if (index == 0) manoObra = value else utilidad = value },
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )
            CampoTextoPedido(
                labels = listOf("Publicidad", "Venta"),
                textos = listOf(publicidad, venta),
                onTextosChange = { index, value -> if (index == 0) publicidad = value else venta = value },
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
                    modifier = Modifier.height(150.dp).fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    val localPath = copiarImagenALocal(context, imagenUri!!) // importante usar !! si ya validaste que no es null

                    val producto = Producto(
                        id = UUID.randomUUID().toString(),
                        codigo = nuevoCodigo.toInt(),
                        nombre = nombre,
                        valorInversion = inversion.toDoubleOrNull() ?: 0.0,
                        valorMargen = margen.toDoubleOrNull() ?: 0.0,
                        valorManoObra = manoObra.toDoubleOrNull() ?: 0.0,
                        valorUtilidad = utilidad.toDoubleOrNull() ?: 0.0,
                        valorPublicidad = publicidad.toDoubleOrNull() ?: 0.0,
                        valorVenta = venta.toDoubleOrNull() ?: 0.0,
                        fotoUriLocal = localPath
                    )

                    productoViewModel.crearProducto(
                        producto = producto,
                        onSuccess = {
                            Toast.makeText(context, "Producto guardado con éxito", Toast.LENGTH_SHORT).show()
                            nombre = ""
                            inversion = ""
                            margen = ""
                            manoObra = ""
                            utilidad = ""
                            publicidad = ""
                            venta = ""
                            imagenUri = null
                            onProductoCreado()
                        },
                        onError = {
                            Toast.makeText(context, "Error al guardar: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            ) {
                Text("Guardar Producto")
            }

        }
    }
}

fun copiarImagenALocal(context: Context, uri: Uri): String {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return ""
        val fileName = "producto_${UUID.randomUUID()}.jpg"
        val file = File(context.cacheDir, fileName)
        file.outputStream().use { output -> inputStream.copyTo(output) }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun generarCodigoAutomatico(productos: List<Producto>): String {
    // Generar el siguiente código disponible, revisando los códigos de los productos existentes
    val codigosExistentes = productos.map { it.codigo }
    var siguienteCodigo = 1
    while (codigosExistentes.contains(siguienteCodigo)) {
        siguienteCodigo++
    }
    return String.format("%03d", siguienteCodigo)
}
