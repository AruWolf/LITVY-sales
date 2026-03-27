package com.litvy.litvysales.ui.purchases.purchaseOrder.create

sealed class UiEvent {
    object Success: UiEvent()
    data class Error(val message: String): UiEvent()
}