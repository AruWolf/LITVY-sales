package com.litvy.litvysales.domain.filter.inventory

data class StockBatchFilter(
    val productId: Int? = null,
    val purchaseItemId: Int? = null,
    val expirationDate: Long? = null,
    val createdAt: Long? = null
)
