package com.litvy.litvysales.ui.purchases.purchaseCreate

import com.litvy.litvysales.ui.components.dialog.AddProductDialogEvent
import com.litvy.litvysales.ui.util.model.ProviderUi
import com.litvy.litvysales.ui.util.model.PurchaseOrderUi

sealed interface PurchaseCreateEvent {

    data object Cancel : PurchaseCreateEvent

    data object Confirm : PurchaseCreateEvent

    data object DismissFeedback : PurchaseCreateEvent

    data object OpenAddProductDialog : PurchaseCreateEvent

    data object CloseAddProductDialog : PurchaseCreateEvent
    data object OpenOrderDialog : PurchaseCreateEvent
    data object CloseOrderDialog : PurchaseCreateEvent

    data class RemoveItem(
        val itemId: String
    ) : PurchaseCreateEvent

    data class UpdateQuantity(
        val itemId: String,
        val quantity: String
    ) : PurchaseCreateEvent

    data class UpdateUnitPrice(
        val itemId: String,
        val price: String
    ) : PurchaseCreateEvent

    data class SelectProvider(
        val provider: ProviderUi
    ) : PurchaseCreateEvent

    data class UpdateSalesRepName(
        val value: String
    ) : PurchaseCreateEvent

    data class SelectInvoiceType(
        val invoiceType: String
    ) : PurchaseCreateEvent

    data class SelectPaymentMethod(
        val paymentMethod: String
    ) : PurchaseCreateEvent

    data class SelectPurchaseOrder(
        val purchaseOrder: PurchaseOrderUi?
    ) : PurchaseCreateEvent

    data object ApplyPurchaseOrderByMerge : PurchaseCreateEvent

    data object ApplyPurchaseOrderByOverwrite : PurchaseCreateEvent

    data object DismissPurchaseOrderConflict : PurchaseCreateEvent

    data class AddProductDialog(
        val event: AddProductDialogEvent
    ) : PurchaseCreateEvent
}
