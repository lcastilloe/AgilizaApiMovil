package com.example.agilizaapp.ui.data

import androidx.compose.ui.graphics.Color

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
    val valorTotal: Double = 0.0,
    val productos: List<ProductoEstadoPedido> = emptyList() // NUEVO
)