package com.litvy.litvysales.ui.sales

import com.litvy.litvysales.domain.model.util.PaymentMethod
import com.litvy.litvysales.ui.components.dialog.AddProductDialogEvent

sealed interface SalesEvent {

    data object OpenAddProductDialog : SalesEvent

    data object CloseAddProductDialog : SalesEvent

    data class AddProductDialog(
        val event: AddProductDialogEvent
    ) : SalesEvent

    data class OpenEditItemDialog(
        val productId: Int
    ) : SalesEvent

    data object CloseEditItemDialog : SalesEvent

    data class UpdateEditItemQuantity(
        val value: String
    ) : SalesEvent

    data class UpdateEditItemUnitPrice(
        val value: String
    ) : SalesEvent

    data object SaveEditedItem : SalesEvent

    data class DeleteItem(
        val productId: Int
    ) : SalesEvent

    data class SelectPaymentMethod(
        val method: PaymentMethod
    ) : SalesEvent

    data object ConfirmSale : SalesEvent

    data object DismissFeedback : SalesEvent
}
