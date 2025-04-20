package com.example.agilizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.agilizaapp.ui.data.ProductoSeleccionadoPedido
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
}
