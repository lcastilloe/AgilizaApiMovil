package com.example.agilizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.agilizaapp.ui.data.ProductoSeleccionadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AnadirPedidoConProductosViewModel : ViewModel() {

    private val _seleccionados = MutableStateFlow<List<ProductoSeleccionadoPedido>>(emptyList())
    val seleccionados: StateFlow<List<ProductoSeleccionadoPedido>> = _seleccionados

    fun agregarProducto(producto: ProductoSeleccionadoPedido) {
        if (_seleccionados.value.none { it.id == producto.id }) {
            _seleccionados.update { it + producto }
        }
    }

    fun eliminarProducto(id: String) {
        _seleccionados.update { it.filterNot { p -> p.id == id } }
    }

    fun calcularTotal(): Double {
        return _seleccionados.value.sumOf { it.valorVenta }
    }

    fun limpiarSeleccionados() {
        _seleccionados.value = emptyList()
    }
}
