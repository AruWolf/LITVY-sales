package com.litvy.litvysales.domain.interfaces.catalog

import com.litvy.litvysales.domain.model.catalog.SubCategory

interface SubCategoryRepository {
    suspend fun create(subCategory: SubCategory)
    suspend fun update(subCategory: SubCategory)
    suspend fun getById(id: Int): SubCategory?
    suspend fun getByCategory(categoryId: Int): List<SubCategory>
    suspend fun existsByNameInCategory(
        name: String,
        categoryId: Int,
    ): Boolean
    suspend fun hasBrands(subCategoryId: Int): Boolean
}