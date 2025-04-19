package com.example.agilizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Pedido(
    val codigo: String = "",
    val estado: String = "",
    val fecha: String = "",
    val hora: String = "",
    val nombreCliente: String = "",
    val numeroCliente: String = "",
    val nombreDestinatario: String = "",
    val numeroDestinatario: String = "",
    val direccion: String = "",
    val barrio: String = "",
    val dePara: String = "",
    val valorDomicilio: Double = 0.0,
    val valorTotal: Double = 0.0
)

class HomeViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos

    init {
        cargarPedidos()
    }

    private fun cargarPedidos() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("usuarios")
            .document(uid)
            .collection("pedidos")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val listaPedidos = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Pedido::class.java)
                }
                _pedidos.value = listaPedidos
            }
    }
}
