package com.example.agilizaapp.ui

import androidx.compose.ui.graphics.Color

data class Pedido(
    val codigo: String,
    val fecha: String,
    val hora: String,
    val estado: String,
    val cliente: String,
    val barrio: String,
    val domicilio: String,
    val productos: String,
    val total: String,
    val backgroundColor: Color
)
