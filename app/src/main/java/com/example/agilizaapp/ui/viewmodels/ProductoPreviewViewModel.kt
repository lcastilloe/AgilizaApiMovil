package com.example.agilizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agilizaapp.ui.data.ProductoSeleccionadoPedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoPreviewViewModel : ViewModel() {
    private val _productos = MutableStateFlow<List<ProductoSeleccionadoPedido>>(emptyList())
    val productos: StateFlow<List<ProductoSeleccionadoPedido>> = _productos

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios")
            .document(uid)
            .collection("productos")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.map { doc ->
                    ProductoSeleccionadoPedido(
                        id = doc.id,
                        codigo = doc.get("codigo").toString(),
                        nombre = doc.getString("nombre") ?: "",
                        valorVenta = doc.getDouble("valorVenta") ?: 0.0
                    )
                }
                _productos.value = lista
            }
    }
}
