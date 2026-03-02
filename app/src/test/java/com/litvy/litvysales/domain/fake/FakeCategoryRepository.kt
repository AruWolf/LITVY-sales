package com.litvy.litvysales.domain.fake

import com.litvy.litvysales.domain.interfaces.catalog.CategoryRepository
import com.litvy.litvysales.domain.model.catalog.Category

class FakeCategoryRepository : CategoryRepository {

    private val categories = mutableListOf<Category>()

    override suspend fun create(category: Category) {
        categories.add(category.copy(id = categories.size + 1))
    }

    override suspend fun update(category: Category) {
        val index = categories.indexOfFirst { it.id == category.id }
        if (index != -1) {
            categories[index] = category
        }
    }

    override suspend fun getById(id: Int): Category? {
        return categories.find { it.id == id }
    }

    override suspend fun getAll(): List<Category> {
        return categories
    }

    override suspend fun existsByName(name: String): Boolean {
        return categories.any { it.name == name }
    }

    override suspend fun hasSubCategories(categoryId: Int): Boolean {
        return false
    }
}