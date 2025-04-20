package com.example.agilizaapp.model  // o .data, si prefieres

data class PedidoTemporal(
    val nombreCliente: String,
    val numeroCliente: String,
    val nombreDestinatario: String,
    val numeroDestinatario: String,
    val direccionEntrega: String,
    val barrioEntrega: String,
    val tarjetaDePara: String,
    val fecha: String,
    val hora: String
)
