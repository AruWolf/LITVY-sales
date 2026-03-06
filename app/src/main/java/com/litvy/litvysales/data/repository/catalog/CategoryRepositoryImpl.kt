package com.litvy.litvysales.data.repository.catalog

import com.litvy.litvysales.data.local.dao.catalog.CategoryDao
import com.litvy.litvysales.data.local.dao.catalog.SubCategoryDao
import com.litvy.litvysales.data.mapper.catalog.toDomain
import com.litvy.litvysales.data.mapper.catalog.toEntity
import com.litvy.litvysales.domain.model.catalog.Category
import com.litvy.litvysales.domain.interfaces.catalog.CategoryRepository

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val subCategoryDao: SubCategoryDao
) : CategoryRepository {

    override suspend fun create(category: Category) {
        categoryDao.insert(category.toEntity())
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun getById(id: Int): Category? {
        return categoryDao.getById(id)?.toDomain()
    }

    override suspend fun existsById(id: Int): Boolean {
        return categoryDao.countById(id) > 0
    }

    override suspend fun getAll(): List<Category> {
        return categoryDao.getAll().map { it.toDomain() }
    }

    override suspend fun existsByName(name: String): Boolean {
        return categoryDao.countByName(name) > 0
    }

    override suspend fun hasSubCategories(categoryId: Int): Boolean {
        return subCategoryDao.countByCategory(categoryId) > 0
    }
}