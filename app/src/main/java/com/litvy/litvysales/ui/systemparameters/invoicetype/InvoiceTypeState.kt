package com.litvy.litvysales.ui.systemparameters.invoicetype

import com.litvy.litvysales.domain.model.purchases.InvoiceType

data class InvoiceTypeState(
    // Inicialización del estado de la lista de tipos de factura
    val items: List<InvoiceType> = emptyList(),

    // Estados del dialog
    val showDialog: Boolean = false,
    val editingItem: InvoiceType? = null,
    val errors: Map<String, String> = emptyMap(),

    // Propiedades
    val code: String = "",
    val description: String = ""
)