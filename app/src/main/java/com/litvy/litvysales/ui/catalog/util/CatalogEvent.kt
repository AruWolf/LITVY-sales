package com.litvy.litvysales.ui.catalog.util

// Eventos del catalogo. Se declaran los posibles eventos que pueden ocurrir dentro del contexto del catalogo
sealed class CatalogEvent {

    object NavigateBack : CatalogEvent()
    object NavigateToCategories : CatalogEvent()
    object NavigateToSubCategories : CatalogEvent()
    object NavigateToBrands : CatalogEvent()
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
        val salePrice: Long,
        val hasExpiration: Boolean,
        val isWeighable: Boolean
    ) : CatalogEvent()

    data class UpdateCategory(
        val id: Int,
        val name: String
    ) : CatalogEvent()

    data class UpdateSubCategory(
        val id: Int,
        val name: String,
        val categoryId: Int
    ) : CatalogEvent()

    data class UpdateBrand(
        val id: Int,
        val name: String,
        val subCategoryId: Int
    ) : CatalogEvent()

    data class UpdateProduct(
        val id: Int,
        val name: String,
        val brandId: Int,
        val purchasePrice: Long,
        val salePrice: Long,
        val hasExpiration: Boolean,
        val isWeighable: Boolean
    ) : CatalogEvent()
}