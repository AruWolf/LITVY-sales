package com.litvy.litvysales.ui.systemparameters.paymentmethod

import com.litvy.litvysales.domain.model.util.PaymentMethod

// Eventos del submodulo de metodos de pago
sealed class PaymentMethodEvent {

    // Eventos de interacción ui
    object OnCreateClick : PaymentMethodEvent()
    object OnDismissDialog : PaymentMethodEvent()

    // Eventos de modificación/creación de propiedades
    data class OnNameChange(val value: String) : PaymentMethodEvent()
    data class OnSurchargeChange(val value: String) : PaymentMethodEvent()

    // Eventos CRUD
    object OnSave : PaymentMethodEvent() // Guardar
    data class OnEdit(val item: PaymentMethod) : PaymentMethodEvent() // Edición
    data class OnDelete(val item: PaymentMethod) : PaymentMethodEvent() // Eliminación
}