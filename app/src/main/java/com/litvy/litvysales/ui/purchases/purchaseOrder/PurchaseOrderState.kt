package com.litvy.litvysales.ui.purchases.purchaseOrder

import com.litvy.litvysales.ui.util.model.PurchaseOrderUi

data class PurchaseOrderState(
    val orders: List<PurchaseOrderUi> = emptyList(),
    val selectedOrder: PurchaseOrderUi? = null,
    val selectedStatus: String = "ALL",
    val search: String = "",
    val feedback: String? = null,
    val isLoading: Boolean = false,

    val isDetailOpen: Boolean = false
)
