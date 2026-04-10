package com.litvy.litvysales.ui.systemparameters.paymentmethod

import com.litvy.litvysales.domain.model.util.PaymentMethod

// Estados ui del submodulo metodos de pago
data class PaymentMethodState(
    // Inicialización de Lista de metodos de pago
    val items: List<PaymentMethod> = emptyList(),

    // Estados del dialog
    val showDialog: Boolean = false,
    val editingItem: PaymentMethod? = null,
    val errors: Map<String, String> = emptyMap(), // Errores en campos del dialog

    // Propiedades
    val name: String = "",
    val surcharge: String = ""
)