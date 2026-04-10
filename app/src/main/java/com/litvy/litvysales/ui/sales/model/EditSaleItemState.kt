package com.litvy.litvysales.ui.sales.model

data class EditSaleItemState(
    val productId: Int,
    val name: String,
    val quantity: String,
    val unitPrice: String,
    val quantityError: String? = null,
    val unitPriceError: String? = null
)
