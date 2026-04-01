package com.litvy.litvysales.ui.purchases.purchaseOrder

import com.litvy.litvysales.ui.util.model.PurchaseOrderUi

data class PurchaseOrderState(
    val orders: List<PurchaseOrderUi> = emptyList(),
    val selectedOrder: PurchaseOrderUi? = null,
    val selectedStatuses: Set<String> = setOf("PENDING", "SENT", "RECEIVED", "CANCELLED"),
    val search: String = "",
    val isLoading: Boolean = false,

    val isDetailOpen: Boolean = false
)
