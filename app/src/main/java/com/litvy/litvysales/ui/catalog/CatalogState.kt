package com.litvy.litvysales.ui.catalog

import com.litvy.litvysales.domain.model.catalog.*
import com.litvy.litvysales.ui.catalog.enum.CatalogLevel

data class CatalogState(

    val level: CatalogLevel = CatalogLevel.CATEGORIES,

    val categories: List<Category> = emptyList(),
    val subCategories: List<SubCategory> = emptyList(),
    val brands: List<Brand> = emptyList(),
    val products: List<Product> = emptyList(),

    val selectedCategoryId: Int? = null,
    val selectedSubCategoryId: Int? = null,
    val selectedBrandId: Int? = null,

    val loading: Boolean = false,
    val error: String? = null
)