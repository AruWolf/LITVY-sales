package com.litvy.litvysales.ui.purchases.purchaseCreate

import com.litvy.litvysales.ui.components.dialog.AddProductDialogState
import com.litvy.litvysales.ui.util.model.ProviderUi
import com.litvy.litvysales.ui.util.model.PurchaseItemUi

data class PurchaseCreateState(

    val provider: ProviderUi? = null,

    val invoiceType: String = "",

    val paymentMethod: String = "",

    val items: List<PurchaseItemUi> = emptyList(),

    val subtotal: Long = 0,
    val tax: Long = 0,
    val total: Long = 0,

    val showAddProductDialog: Boolean = false,

    val addProductState: AddProductDialogState = AddProductDialogState()
)