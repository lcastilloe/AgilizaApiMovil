package com.example.agilizaapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agilizaapp.ui.data.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class ProductoViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("usuarios")
            .document(uid)
            .collection("productos")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.map { doc ->
                    Producto(
                        id = doc.id,
                        codigo = (doc.getLong("codigo") ?: 0).toInt(),
                        nombre = doc.getString("nombre") ?: "",
                        valorInversion = doc.getDouble("valorInversion") ?: 0.0,
                        valorMargen = doc.getDouble("valorMargen") ?: 0.0,
                        valorManoObra = doc.getDouble("valorManoObra") ?: 0.0,
                        valorUtilidad = doc.getDouble("valorUtilidad") ?: 0.0,
                        valorPublicidad = doc.getDouble("valorPublicidad") ?: 0.0,
                        valorVenta = doc.getDouble("valorVenta") ?: 0.0,
                        fotoUrl = doc.getString("fotoUrl") ?: ""
                    )
                }
                _productos.value = lista
            }
    }

    fun crearProducto(
        producto: Producto,
        imagenUri: Uri?,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return
        val productoRef = db.collection("usuarios")
            .document(uid)
            .collection("productos")
            .document()

        viewModelScope.launch {
            if (imagenUri != null) {
                val ruta = "productos/$uid/${UUID.randomUUID()}.jpg"
                val ref = storage.reference.child(ruta)

                ref.putFile(imagenUri)
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { uri ->
                            val productoConUrl = producto.copy(fotoUrl = uri.toString())
                            guardarEnFirestore(productoRef.id, productoConUrl, onSuccess, onError)
                        }
                    }
                    .addOnFailureListener { onError(it) }
            } else {
                guardarEnFirestore(productoRef.id, producto, onSuccess, onError)
            }
        }
    }

    private fun guardarEnFirestore(
        id: String,
        producto: Producto,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios")
            .document(uid)
            .collection("productos")
            .document(id)
            .set(producto.copy(id = id))
            .addOnSuccessListener {
                cargarProductos()
                onSuccess()
            }
            .addOnFailureListener { onError(it) }
    }
}
