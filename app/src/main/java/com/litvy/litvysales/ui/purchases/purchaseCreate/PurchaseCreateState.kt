package com.litvy.litvysales.ui.purchases.purchaseCreate

import com.litvy.litvysales.ui.components.dialog.AddProductDialogState
import com.litvy.litvysales.ui.util.model.ProviderUi
import com.litvy.litvysales.ui.util.model.PurchaseOrderUi
import com.litvy.litvysales.ui.util.model.PurchaseItemUi

data class PurchaseCreateState(
    val providers: List<ProviderUi> = emptyList(),
    val invoiceTypeOptions: List<String> = emptyList(),
    val paymentMethodOptions: List<String> = emptyList(),
    val purchaseOrders: List<PurchaseOrderUi> = emptyList(),

    val provider: ProviderUi? = null,
    val salesRepName: String = "",
    val invoiceType: String = "",
    val paymentMethod: String = "",
    val selectedPurchaseOrder: PurchaseOrderUi? = null,
    val pendingPurchaseOrder: PurchaseOrderUi? = null,

    val items: List<PurchaseItemUi> = emptyList(),

    val subtotalInCents: Long = 0,
    val taxInCents: Long = 0,
    val totalInCents: Long = 0,

    val providerError: String? = null,
    val salesRepError: String? = null,
    val invoiceTypeError: String? = null,
    val paymentMethodError: String? = null,
    val itemsError: String? = null,
    val feedbackMessage: String? = null,
    val isSubmitting: Boolean = false,
    val showOrderConflictDialog: Boolean = false,
    val showOrderDialog: Boolean = false,

    val showAddProductDialog: Boolean = false,
    val addProductState: AddProductDialogState = AddProductDialogState()
) {
    val canConfirm: Boolean
        get() = provider != null &&
            salesRepName.isNotBlank() &&
            invoiceType.isNotBlank() &&
            paymentMethod.isNotBlank() &&
            items.isNotEmpty() &&
            !isSubmitting
}
