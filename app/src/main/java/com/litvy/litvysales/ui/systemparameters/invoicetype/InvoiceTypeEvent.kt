package com.litvy.litvysales.ui.systemparameters.invoicetype

import com.litvy.litvysales.domain.model.purchases.InvoiceType

sealed class InvoiceTypeEvent {

    // Eventos de interacción ui
    object OnCreateClick: InvoiceTypeEvent()
    object OnDismissDialog: InvoiceTypeEvent()

    // Eventos de modificación/creación de propiedades
    data class OnCodeChange(val value: String): InvoiceTypeEvent()
    data class OnDescriptionChange(val value: String): InvoiceTypeEvent()

    // Eventos CRUD
    object OnSave: InvoiceTypeEvent()
    data class OnEdit(val item: InvoiceType): InvoiceTypeEvent()
    data class OnDelete(val item: InvoiceType): InvoiceTypeEvent()
}