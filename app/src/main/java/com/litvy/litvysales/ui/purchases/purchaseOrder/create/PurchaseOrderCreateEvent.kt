package com.litvy.litvysales.ui.purchases.purchaseOrder.create

sealed interface PurchaseOrderCreateEvent {

    data class OnProviderSelected(val providerId: Int) : PurchaseOrderCreateEvent
    data class OnDateChange(val value: String) : PurchaseOrderCreateEvent

    object OnAddItem : PurchaseOrderCreateEvent
    data class OnRemoveItem(val index: Int) : PurchaseOrderCreateEvent

    data class OnProductSelected(val index: Int, val productId: Int) : PurchaseOrderCreateEvent
    data class OnQuantityChange(val index: Int, val quantity: String) : PurchaseOrderCreateEvent

    data class OnOpenProductDialog(val index: Int) : PurchaseOrderCreateEvent
    object OnCloseProductDialog : PurchaseOrderCreateEvent
    data class OnProductPicked(val productId: Int) : PurchaseOrderCreateEvent
    data class OnProductSearchChange(val value: String) : PurchaseOrderCreateEvent

    object OnSave : PurchaseOrderCreateEvent

    object LoadCategories : PurchaseOrderCreateEvent
    data class OnCategorySelected(val id: Int) : PurchaseOrderCreateEvent
    data class OnSubCategorySelected(val id: Int) : PurchaseOrderCreateEvent
    data class OnBrandSelected(val id: Int?) : PurchaseOrderCreateEvent

}