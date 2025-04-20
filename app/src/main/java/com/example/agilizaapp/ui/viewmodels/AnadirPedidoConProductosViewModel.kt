package com.example.agilizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.agilizaapp.model.PedidoTemporal
import com.example.agilizaapp.ui.data.ProductoSeleccionadoPedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AnadirPedidoConProductosViewModel : ViewModel() {

    private val _seleccionados = MutableStateFlow<List<ProductoSeleccionadoPedido>>(emptyList())
    val seleccionados: StateFlow<List<ProductoSeleccionadoPedido>> = _seleccionados

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    fun agregarProducto(producto: ProductoSeleccionadoPedido) {
        _seleccionados.update { it + producto }
        recalcularTotal()
    }

    fun eliminarProducto(id: String) {
        _seleccionados.update { it.filterNot { p -> p.id == id } }
        recalcularTotal()
    }

    fun limpiarSeleccionados() {
        _seleccionados.value = emptyList()
        _total.value = 0.0
    }

    private fun recalcularTotal() {
        _total.value = _seleccionados.value.sumOf { it.valorVenta }
    }

    fun guardarPedidoCompleto(
        codigo: String,
        pedidoTemporal: PedidoTemporal,
        total: Double,
        productos: List<ProductoSeleccionadoPedido>,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val pedidoMap = hashMapOf(
            "codigo" to codigo,
            "estado" to "Inicio",
            "fecha" to pedidoTemporal.fecha,
            "hora" to pedidoTemporal.hora,
            "nombreCliente" to pedidoTemporal.nombreCliente,
            "numeroCliente" to pedidoTemporal.numeroCliente,
            "nombreDestinatario" to pedidoTemporal.nombreDestinatario,
            "numeroDestinatario" to pedidoTemporal.numeroDestinatario,
            "direccion" to pedidoTemporal.direccionEntrega,
            "barrio" to pedidoTemporal.barrioEntrega,
            "dePara" to pedidoTemporal.tarjetaDePara,
            "valorDomicilio" to 7000.0,
            "valorTotal" to total
        )

        val db = FirebaseFirestore.getInstance()
        val pedidosRef = db.collection("usuarios").document(uid).collection("pedidos")

        pedidosRef.add(pedidoMap)
            .addOnSuccessListener { docRef ->
                val productosRef = docRef.collection("productos")
                val batch = db.batch()

                productos.forEach { producto ->
                    val prod = hashMapOf(
                        "codigo" to producto.codigo,
                        "nombre" to producto.nombre,
                        "valorVenta" to producto.valorVenta
                    )
                    batch.set(productosRef.document(), prod)
                }

                batch.commit()
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError(it) }
            }
            .addOnFailureListener { onError(it) }
    }
}
