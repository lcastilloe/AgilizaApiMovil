package com.example.agilizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agilizaapp.ui.data.Pedido
import com.example.agilizaapp.ui.data.ProductoEstadoPedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos

    init {
        cargarPedidos()
    }

    fun cargarPedidos() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("usuarios")
                    .document(uid)
                    .collection("pedidos")
                    .get()
                    .await()

                val pedidosList = mutableListOf<Pedido>()
                val hoy = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time

                val formatoFechaHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                for (doc in snapshot.documents) {
                    val pedido = doc.toObject(Pedido::class.java)?.copy(
                        codigo = doc.getString("codigo") ?: "",
                        estado = doc.getString("estado") ?: "Inicio" // ← importante
                    ) ?: continue



                    val fechaPedido = formatoFecha.parse(pedido.fecha)
                    if (fechaPedido != null && fechaPedido.before(hoy)) continue

                    var productos: List<ProductoEstadoPedido> = emptyList()
                    var intentos = 0
                    while (productos.isEmpty() && intentos < 5) {
                        val productosSnapshot = doc.reference.collection("productos").get().await()
                        productos = productosSnapshot.map { productoDoc ->
                            ProductoEstadoPedido(
                                codigo = productoDoc.getString("codigo") ?: "",
                                estado = productoDoc.getString("estado") ?: "Inicio",
                                precio = "$${productoDoc.getDouble("valorVenta") ?: 0.0}"
                            )
                        }
                        intentos++
                    }

                    pedidosList.add(pedido.copy(productos = productos))
                }

                // Ordenar por fecha + hora combinadas
                _pedidos.value = pedidosList.sortedBy { pedido ->
                    formatoFechaHora.parse("${pedido.fecha} ${pedido.hora}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
