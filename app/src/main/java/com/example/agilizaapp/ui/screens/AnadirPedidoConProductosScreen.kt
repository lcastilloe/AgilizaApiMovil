package com.example.agilizaapp.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agilizaapp.ui.components.AnadirPedidoConProductos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AnadirPedidoConProductosScreen() {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    var codigoPedido by remember { mutableStateOf("...") }

    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("usuarios")
                .document(uid)
                .collection("pedidos")
                .get()
                .addOnSuccessListener { result ->
                    val codigos = result.mapNotNull { it.getString("codigo") }
                    val letras = ('A'..'Z')
                    var generado = false
                    for (letra in letras) {
                        for (numero in 1..99) {
                            val posible = letra + numero.toString().padStart(2, '0')
                            if (posible !in codigos) {
                                codigoPedido = posible
                                generado = true
                                break
                            }
                        }
                        if (generado) break
                    }
                }
        }
    }

    //AnadirPedidoConProductos(codigo = codigoPedido)
}


