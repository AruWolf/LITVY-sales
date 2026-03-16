package com.litvy.litvysales.ui.purchases.purchaseCreate

import com.litvy.litvysales.ui.util.model.ProviderUi

sealed class PurchaseCreateEvent {

    data object Cancel : PurchaseCreateEvent()

    data object Confirm : PurchaseCreateEvent()

    data object AddItem : PurchaseCreateEvent()

    data object OpenProviderSelector : PurchaseCreateEvent()

    data class RemoveItem(
        val itemId: String
    ) : PurchaseCreateEvent()

    data class UpdateQuantity(
        val itemId: Int,
        val quantity: Double
    ) : PurchaseCreateEvent()

    data class UpdateUnitPrice(
        val itemId: Int,
        val price: Double
    ) : PurchaseCreateEvent()

    data class SelectProvider(
        val provider: ProviderUi
    ) : PurchaseCreateEvent()

    data class SelectInvoiceType(
        val invoiceType: String
    ) : PurchaseCreateEvent()

    data class SelectPaymentMethod(
        val paymentMethod: String
    ) : PurchaseCreateEvent()
}