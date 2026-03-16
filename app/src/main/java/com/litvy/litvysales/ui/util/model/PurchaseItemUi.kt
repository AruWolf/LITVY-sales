package com.litvy.litvysales.ui.util.model

data class PurchaseItemUi(

    val uiId: String,

    val productId: Int,

    val productName: String,

    val quantity: Double,

    val unitPrice: Long,

    val total: Long
)