package com.litvy.litvysales.ui.purchases.purchaseOrder.create

import com.litvy.litvysales.domain.model.catalog.Brand
import com.litvy.litvysales.domain.model.catalog.Category
import com.litvy.litvysales.domain.model.catalog.Product
import com.litvy.litvysales.domain.model.catalog.ProductWithBrand
import com.litvy.litvysales.domain.model.catalog.SubCategory
import com.litvy.litvysales.domain.model.purchases.Provider
import com.litvy.litvysales.ui.util.model.PurchaseOrderItemUi

data class PurchaseOrderCreateState(
    val providers: List<Provider> = emptyList(),
    val products: List<ProductWithBrand> = emptyList(),

    val selectedProviderId: Int? = null,
    val expectedDate: String = "",

    val items: List<PurchaseOrderItemUi> = emptyList(),

    val isLoading: Boolean = false,
    val error: String? = null,

    val isProductDialogOpen: Boolean = false,
    val selectedItemIndex: Int? = null,
    val productSearch: String = "",

    val categories: List<Category> = emptyList(),
    val subCategories: List<SubCategory> = emptyList(),
    val brands: List<Brand> = emptyList(),

    val selectedCategoryId: Int? = null,
    val selectedSubCategoryId: Int? = null,
    val selectedBrandId: Int? = null,
    val filteredProducts: List<ProductWithBrand> = emptyList()
)
