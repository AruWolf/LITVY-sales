package com.litvy.litvysales.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litvy.litvysales.domain.useCase.catalog.brand.*
import com.litvy.litvysales.domain.useCase.catalog.category.*
import com.litvy.litvysales.domain.useCase.catalog.product.*
import com.litvy.litvysales.domain.useCase.catalog.subCategory.*

class CatalogViewModelFactory(
    private val getCategories: GetCategoriesUseCase,
    private val getSubCategories: GetSubCategoriesByCategoryUseCase,
    private val getBrands: GetBrandBySubCategoryUseCase,
    private val getProducts: GetProductByBrandUseCase,

    private val createCategory: CreateCategoryUseCase,
    private val createSubCategory: CreateSubCategoryUseCase,
    private val createBrand: CreateBrandUseCase,
    private val createProduct: CreateProductUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {

            return CatalogViewModel(
                getCategories,
                getSubCategories,
                getBrands,
                getProducts,
                createCategory,
                createSubCategory,
                createBrand,
                createProduct
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}