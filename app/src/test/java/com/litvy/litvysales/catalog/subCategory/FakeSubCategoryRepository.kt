package com.litvy.litvysales.catalog.subCategory

import com.litvy.litvysales.domain.interfaces.catalog.SubCategoryRepository
import com.litvy.litvysales.domain.model.catalog.SubCategory

class FakeSubCategoryRepository : SubCategoryRepository {

    private val subCategories = mutableListOf<SubCategory>()

    override suspend fun create(subCategory: SubCategory) {
        subCategories.add(subCategory)
    }

    override suspend fun update(subCategory: SubCategory) {}

    override suspend fun getById(id: Int): SubCategory? {
        return subCategories.find { it.id == id }
    }

    override suspend fun getByCategory(categoryId: Int): List<SubCategory> {
        return subCategories.filter { it.categoryId == categoryId }
    }

    override suspend fun existsByNameInCategory(name: String, categoryId: Int): Boolean {
        return subCategories.any {
            it.name == name && it.categoryId == categoryId
        }
    }

    override suspend fun hasBrands(subCategoryId: Int): Boolean {
        return false
    }
}
