package com.litvy.litvysales.ui.components.dialog

import com.litvy.litvysales.ui.util.model.ProductUi

sealed class AddProductDialogEvent {

    data class SelectCategory(
        val categoryId: Int?
    ) : AddProductDialogEvent()

    data class SelectSubCategory(
        val subCategoryId: Int?
    ) : AddProductDialogEvent()

    data class SelectBrand(
        val brandId: Int?
    ) : AddProductDialogEvent()

    data class SearchChanged(
        val query: String
    ) : AddProductDialogEvent()

    data class SelectProduct(
        val product: ProductUi
    ) : AddProductDialogEvent()

    data class QuantityChanged(
        val value: String
    ) : AddProductDialogEvent()

    data class PriceChanged(
        val value: String
    ) : AddProductDialogEvent()

    data object ScanBarcode : AddProductDialogEvent()

    data object Confirm : AddProductDialogEvent()

    data object Cancel : AddProductDialogEvent()
}
