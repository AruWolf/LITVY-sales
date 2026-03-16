package com.litvy.litvysales.ui.inventory.model

data class InventoryProductItem(
    val id: Int,
    val productName: String,
    val totalStock: Double,
    val batchCount: Int,
    val warnings: List<InventoryProductWarning>
)


