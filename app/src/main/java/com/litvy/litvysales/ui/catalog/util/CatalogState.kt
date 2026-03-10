package com.litvy.litvysales.ui.catalog.util

import com.litvy.litvysales.domain.model.catalog.Brand
import com.litvy.litvysales.domain.model.catalog.Category
import com.litvy.litvysales.domain.model.catalog.Product
import com.litvy.litvysales.domain.model.catalog.SubCategory

// Estados accesibles por Catalog Screen
data class CatalogState(

    val level: CatalogLevel = CatalogLevel.CATEGORIES,

    val categories: List<Category> = emptyList(),
    val subCategories: List<SubCategory> = emptyList(),
    val brands: List<Brand> = emptyList(),
    val products: List<Product> = emptyList(),

    val selectedCategoryId: Int? = null,
    val selectedSubCategoryId: Int? = null,
    val selectedBrandId: Int? = null,
    val selectedCategoryName: String? = null,
    val selectedSubCategoryName: String? = null,
    val selectedBrandName: String? = null,

    val loading: Boolean = false,
    val error: String? = null
)