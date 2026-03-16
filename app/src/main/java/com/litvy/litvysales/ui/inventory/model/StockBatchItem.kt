package com.litvy.litvysales.ui.inventory.model

data class StockBatchItem(
    val id: Int,
    val quantity: Double,
    val expirationDate: Long?,
    val expirationStatus: ExpirationStatus
)
