package com.litvy.litvysales.catalog.brand

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.model.catalog.Brand

class FakeBrandRepository : BrandRepository {

    private val brands = mutableListOf<Brand>()

    override suspend fun insert(brand: Brand) {
        brands.add(brand)
    }

    override suspend fun update(brand: Brand) {
        val index = brands.indexOfFirst { it.id == brand.id }
        if (index != -1) {
            brands[index] = brand
        }
    }

    override suspend fun getBySubCategory(subCategoryId: Int): List<Brand> {
        return brands.filter { it.subCategoryId == subCategoryId }
    }

    override suspend fun existsByNameInSubCategory(name: String, subCategoryId: Int): Boolean {
        return brands.any {
            it.name == name && it.subCategoryId == subCategoryId
        }
    }

    override suspend fun getById(id: Int): Brand? {
        return brands.find { it.id == id }
    }

    override suspend fun countBySubCategory(subCategoryId: Int): Int {
        return brands.count { it.subCategoryId == subCategoryId }
    }
}