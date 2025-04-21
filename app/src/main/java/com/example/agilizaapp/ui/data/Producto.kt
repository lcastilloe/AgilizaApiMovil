package com.example.agilizaapp.ui.data

data class Producto(
    val id: String = "",
    val codigo: Int = 0,
    val nombre: String = "",
    val valorInversion: Double = 0.0,
    val valorMargen: Double = 0.0,
    val valorManoObra: Double = 0.0,
    val valorUtilidad: Double = 0.0,
    val valorPublicidad: Double = 0.0,
    val valorVenta: Double = 0.0,
    val fotoUriLocal: String = ""  // ← este campo es importante
)
