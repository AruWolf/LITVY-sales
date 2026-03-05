package com.litvy.litvysales.data.repository.catalog

import com.litvy.litvysales.data.local.dao.catalog.BrandDao
import com.litvy.litvysales.data.local.dao.catalog.SubCategoryDao
import com.litvy.litvysales.domain.interfaces.catalog.SubCategoryRepository
import com.litvy.litvysales.domain.model.catalog.SubCategory
import com.litvy.litvysales.data.mapper.catalog.toEntity
import com.litvy.litvysales.data.mapper.catalog.toDomain

class SubCategoryRepository(
    private val subCategoryDao: SubCategoryDao,
    private val brandDao: BrandDao
): SubCategoryRepository {

    override suspend fun create(subCategory: SubCategory) {
        subCategoryDao.insert(subCategory.toEntity())
    }

    override suspend fun update(subCategory: SubCategory) {
        subCategoryDao.update(subCategory.toEntity())
    }

    override suspend fun getById(id: Int): SubCategory? {
        return subCategoryDao.getById(id)?.toDomain()
    }

    override suspend fun getByCategory(categoryId: Int): List<SubCategory> {
        return subCategoryDao.getByCategory(categoryId).map { it.toDomain() }
    }

    override suspend fun existsByNameInCategory(
        name: String,
        categoryId: Int
    ): Boolean {
        return subCategoryDao.countByName(name, categoryId) > 0
    }

    override suspend fun hasBrands(subCategoryId: Int): Boolean {
        return brandDao.countBySubCategory(subCategoryId) > 0
    }
}