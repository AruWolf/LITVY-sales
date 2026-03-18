package com.litvy.litvysales.ui.util.model

data class PurchaseOrderUi(
    val id: Int,
    val providerId: Int,
    val providerName: String,
    val status: String,
    val expectedDeliveryLabel: String?,
    val items: List<PurchaseOrderItemUi>
)

data class PurchaseOrderItemUi(
    val productId: Int,
    val productName: String,
    val quantity: Double,
    val suggestedUnitPriceInCents: Long?
)
