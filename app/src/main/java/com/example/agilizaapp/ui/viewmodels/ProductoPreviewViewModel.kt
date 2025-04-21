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
                        fotoUriLocal = doc.getString("fotoUriLocal") ?: ""

                    )
                }
                _productos.value = lista
            }
    }

    fun crearProducto(
        producto: Producto,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return
        val productoRef = db.collection("usuarios")
            .document(uid)
            .collection("productos")
            .document()

        db.collection("usuarios")
            .document(uid)
            .collection("productos")
            .document(productoRef.id)
            .set(producto.copy(id = productoRef.id))
            .addOnSuccessListener {
                cargarProductos()
                onSuccess()
            }
            .addOnFailureListener { onError(it) }
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
