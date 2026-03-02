package com.litvy.litvysales.data.repository

import com.litvy.litvysales.data.local.dao.BrandDao
import com.litvy.litvysales.data.local.dao.ProductDao
import com.litvy.litvysales.data.mapper.toDomain
import com.litvy.litvysales.data.mapper.toEntity
import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.model.catalog.Brand

class BrandRepository(
    private val brandDao: BrandDao,
    private val productDao: ProductDao
): BrandRepository{
    override suspend fun insert(brand: Brand) {
        brandDao.insert(brand.toEntity())
    }

    override suspend fun getBySubCategory(subCategoryId: Int): List<Brand> {
        return brandDao.getBySubCategory(subCategoryId).map {it.toDomain()}
    }

    override suspend fun existsByNameInSubCategory(name: String, subCategoryId: Int): Boolean {
        return brandDao.existsByNameInSubCategory(name, subCategoryId)
    }

    override suspend fun countBySubCategory(subCategoryId: Int): Int {
        return brandDao.countBySubCategory(subCategoryId)
    }
}