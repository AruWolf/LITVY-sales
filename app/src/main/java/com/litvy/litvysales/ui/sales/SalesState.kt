package com.litvy.litvysales.ui.sales

import com.litvy.litvysales.domain.model.util.PaymentMethod
import com.litvy.litvysales.ui.components.dialog.AddProductDialogState
import com.litvy.litvysales.ui.sales.model.SaleDraft
import com.litvy.litvysales.ui.sales.model.EditSaleItemState

data class SalesState(
    val draft: SaleDraft = SaleDraft(),
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val paymentMethodError: String? = null,
    val itemsError: String? = null,
    val feedbackMessage: String? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val showAddProductDialog: Boolean = false,
    val addProductState: AddProductDialogState = AddProductDialogState(),
    val showEditItemDialog: Boolean = false,
    val editItemState: EditSaleItemState? = null,

) {
    val selectedPaymentMethodId: Int?
        get() = draft.payments.firstOrNull()?.paymentMethodId

    val canConfirm: Boolean
        get() = draft.items.isNotEmpty() &&
            selectedPaymentMethodId != null &&
            !isSubmitting
}
