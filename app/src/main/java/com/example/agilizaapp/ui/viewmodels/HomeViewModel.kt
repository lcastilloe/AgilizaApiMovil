package com.example.agilizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.agilizaapp.ui.data.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



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
