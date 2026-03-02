package com.litvy.litvysales.domain.interfaces.catalog

import com.litvy.litvysales.domain.model.catalog.Brand

interface BrandRepository {
    suspend fun insert(brand: Brand)
    suspend fun getBySubCategory(subCategoryId: Int): List<Brand>
    suspend fun existsByNameInSubCategory(name: String, subCategoryId: Int): Boolean
    //TODO: Terminar getbyId para caso de uso de actulizar
    //suspend fun getById
    suspend fun countBySubCategory(subCategoryId: Int): Int
}