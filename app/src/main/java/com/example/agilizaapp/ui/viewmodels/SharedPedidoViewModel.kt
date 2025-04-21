package com.example.agilizaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.agilizaapp.model.PedidoTemporal

class SharedPedidoViewModel : ViewModel() {
    var pedidoTemporal: PedidoTemporal? = null
    var codigoGenerado: String = "..."
}