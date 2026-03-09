package com.litvy.litvysales.ui.catalog

sealed class CatalogEvent {

    object NavigateBack : CatalogEvent()
    data class SelectCategory(val categoryId: Int) : CatalogEvent()
    data class SelectSubCategory(val subCategoryId: Int) : CatalogEvent()
    data class SelectBrand(val brandId: Int) : CatalogEvent()

    data class CreateCategory(val name: String) : CatalogEvent()

    data class CreateSubCategory(
        val name: String,
        val categoryId: Int
    ) : CatalogEvent()

    data class CreateBrand(
        val name: String,
        val subCategoryId: Int
    ) : CatalogEvent()

    data class CreateProduct(
        val name: String,
        val brandId: Int,
        val purchasePrice: Long,
        val salePrice: Long
    ) : CatalogEvent()
}