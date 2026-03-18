package com.litvy.litvysales.ui.components.dialog

import com.litvy.litvysales.ui.util.model.CatalogOptionUi
import com.litvy.litvysales.ui.util.model.ProductUi

data class AddProductDialogState(
    val categories: List<CatalogOptionUi> = emptyList(),
    val subCategories: List<CatalogOptionUi> = emptyList(),
    val brands: List<CatalogOptionUi> = emptyList(),
    val selectedCategoryId: Int? = null,
    val selectedSubCategoryId: Int? = null,
    val selectedBrandId: Int? = null,
    val searchQuery: String = "",
    val products: List<ProductUi> = emptyList(),
    val selectedProduct: ProductUi? = null,
    val quantity: String = "1",
    val unitPrice: String = "",
    val showResults: Boolean = true,
    val searchError: String? = null,
    val quantityError: String? = null,
    val unitPriceError: String? = null
)
