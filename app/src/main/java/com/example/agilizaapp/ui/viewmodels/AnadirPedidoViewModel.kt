package com.example.agilizaapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AnadirPedidoViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun guardarPedidoDeEjemplo(
        fecha: String,
        hora: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError(Exception("Usuario no autenticado"))
            return
        }

        val pedido = hashMapOf(
            "codigo" to "A10",
            "estado" to "Inicio",
            "fecha" to fecha,
            "hora" to hora,
            "nombreCliente" to "Mateo",
            "numeroCliente" to "3001234567",
            "nombreDestinatario" to "Laura",
            "numeroDestinatario" to "3019876543",
            "direccion" to "Cra 12 #34-56",
            "barrio" to "Santa Clara",
            "dePara" to "De Mateo para Laura",
            "valorDomicilio" to 7000.0,
            "valorTotal" to 150000.0
        )

        firestore.collection("usuarios")
            .document(uid)
            .collection("pedidos")
            .add(pedido)
            .addOnSuccessListener {
                Log.d("Firestore", "Pedido guardado correctamente")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al guardar pedido", e)
                onError(e)
            }
    }
}
