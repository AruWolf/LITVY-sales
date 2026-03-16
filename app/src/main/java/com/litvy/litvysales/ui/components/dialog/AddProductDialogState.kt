package com.litvy.litvysales.ui.components.dialog

import com.litvy.litvysales.ui.util.model.*

data class AddProductDialogState(

    val searchQuery: String = "",

    val products: List<ProductUi> = emptyList(),

    val selectedProduct: ProductUi? = null,

    val quantity: String = "1",

    val unitPrice: String = "",

    val showResults: Boolean = true
)