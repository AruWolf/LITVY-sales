package com.litvy.litvysales.data.repository.catalog

import com.litvy.litvysales.data.local.dao.catalog.BrandDao
import com.litvy.litvysales.data.local.dao.catalog.ProductDao
import com.litvy.litvysales.data.mapper.catalog.toDomain
import com.litvy.litvysales.data.mapper.catalog.toEntity
import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.model.catalog.Brand

class BrandRepositoryImpl(
    private val brandDao: BrandDao,
    private val productDao: ProductDao
): BrandRepository{
    override suspend fun insert(brand: Brand) {
        brandDao.insert(brand.toEntity())
    }

    override suspend fun update(brand: Brand) {
        brandDao.update(brand.toEntity())
    }

    override suspend fun getBySubCategory(subCategoryId: Int): List<Brand> {
        return brandDao.getBySubCategory(subCategoryId).map {it.toDomain()}
    }

    override suspend fun existsByNameInSubCategory(name: String, subCategoryId: Int): Boolean {
        return brandDao.countByNameInSubCategory(name, subCategoryId) > 0    }

    override suspend fun getById(id: Int): Brand?{
        return brandDao.getById(id)?.toDomain()
    }

    override suspend fun countBySubCategory(subCategoryId: Int): Int {
        return brandDao.countBySubCategory(subCategoryId)
    }
}