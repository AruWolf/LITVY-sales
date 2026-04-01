package com.litvy.litvysales.ui.purchases.purchaseOrder

sealed class PurchaseOrderEvent {
    data class OnSearchChange(val value: String): PurchaseOrderEvent()
    data class OnStatusChange(val status: String): PurchaseOrderEvent()
    data class OnSelectOrder(val orderId: Int): PurchaseOrderEvent()
    data class OnToggleStatus(val status: String): PurchaseOrderEvent()

    object OnMarkSent: PurchaseOrderEvent()
    object OnMarkReceived: PurchaseOrderEvent()
    object OnCreateOrder: PurchaseOrderEvent()
    object OnCloseDetail: PurchaseOrderEvent()
}